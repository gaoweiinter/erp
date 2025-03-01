#!/usr/bin/env python3
import sys
import subprocess
import importlib.util
from typing import List, Tuple

def check_and_install_dependencies() -> None:
    """
    检查并安装所需的依赖包
    """
    required_packages: List[Tuple[str, str]] = [
        ('ntplib', 'ntplib>=0.4.0'),
        ('pytz', 'pytz>=2024.1'),
        ('tzlocal', 'tzlocal>=5.2')
    ]
    
    missing_packages = []
    
    # 检查每个依赖包
    for package_name, package_spec in required_packages:
        if importlib.util.find_spec(package_name) is None:
            missing_packages.append(package_spec)
    
    # 如果有缺失的包，尝试安装
    if missing_packages:
        print("检测到缺少必要的依赖包，正在安装...", file=sys.stderr)
        try:
            subprocess.check_call([
                sys.executable, 
                '-m', 
                'pip', 
                'install',
                *missing_packages
            ])
            print("依赖包安装完成。", file=sys.stderr)
        except subprocess.CalledProcessError as e:
            print(f"安装依赖包失败: {str(e)}", file=sys.stderr)
            sys.exit(1)
        except Exception as e:
            print(f"安装过程中发生错误: {str(e)}", file=sys.stderr)
            sys.exit(1)

# 在导入其他模块之前先检查依赖
check_and_install_dependencies()

# 导入其他需要的模块
import argparse
import datetime
import ntplib
import pytz
import time
import tzlocal

def get_system_timezone() -> str:
    """
    获取系统当前时区
    
    Returns:
        str: 时区名称，例如 'Asia/Shanghai'
    """
    try:
        return str(tzlocal.get_localzone())
    except Exception:
        # 如果无法获取系统时区，回退到使用时间戳的偏移量来推测
        offset = time.timezone if (time.localtime().tm_isdst == 0) else time.altzone
        offset = -offset  # 转换为正确的偏移方向
        hours = offset // 3600
        
        # 查找最接近的时区
        for tz in pytz.all_timezones:
            try:
                tz_obj = pytz.timezone(tz)
                tz_offset = int(datetime.datetime.now(tz_obj).utcoffset().total_seconds())
                if tz_offset == offset:
                    return tz
            except:
                continue
                
        # 如果找不到匹配的时区，返回UTC
        return 'UTC'

def get_network_time(timezone: str = None) -> datetime.datetime:
    """
    从NTP服务器获取网络时间
    
    Args:
        timezone: 时区名称，如果为None则使用系统时区。例如：'Asia/Shanghai'
    
    Returns:
        datetime.datetime: 当前网络时间
    
    Raises:
        Exception: 当NTP服务器连接失败或时区无效时抛出
    """
    if timezone is None:
        timezone = get_system_timezone()
        
    try:
        # 创建NTP客户端
        ntp_client = ntplib.NTPClient()
        
        # 尝试多个NTP服务器
        ntp_servers = [
            'pool.ntp.org',
            'time.google.com',
            'time.windows.com',
            'time.apple.com'
        ]
        
        response = None
        last_error = None
        for server in ntp_servers:
            try:
                response = ntp_client.request(server, timeout=2)
                break
            except Exception as e:
                last_error = e
                continue
                
        if response is None:
            raise Exception(f"无法连接到任何NTP服务器: {str(last_error)}")
            
        # 获取UTC时间
        utc_time = datetime.datetime.fromtimestamp(response.tx_time, pytz.UTC)
        
        # 转换到指定时区
        try:
            local_tz = pytz.timezone(timezone)
            local_time = utc_time.astimezone(local_tz)
            return local_time
        except pytz.exceptions.UnknownTimeZoneError:
            raise Exception(f"无效的时区: {timezone}")
            
    except Exception as e:
        raise Exception(f"获取网络时间失败: {str(e)}")

def main():
    parser = argparse.ArgumentParser(description='获取网络时间')
    parser.add_argument('--timezone', '-t', 
                      default=None,
                      help='时区 (例如: Asia/Shanghai)，默认使用系统时区')
    parser.add_argument('--format', '-f',
                      default='%Y-%m-%d %H:%M:%S %Z',
                      help='时间格式 (默认: YYYY-MM-DD HH:MM:SS TZ)')
    parser.add_argument('--debug', '-d',
                      action='store_true',
                      help='显示调试信息')
    
    args = parser.parse_args()
    
    try:
        if args.debug:
            system_tz = get_system_timezone()
            print(f"系统时区: {system_tz}", file=sys.stderr)
            
        network_time = get_network_time(args.timezone)
        
        if args.debug:
            print(f"使用时区: {network_time.tzinfo}", file=sys.stderr)
            
        print(network_time.strftime(args.format))
    except Exception as e:
        print(f"错误: {str(e)}", file=sys.stderr)
        sys.exit(1)

if __name__ == '__main__':
    main() 