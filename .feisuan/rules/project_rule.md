
# 开发规范指南

本项目为基于 **NeoForge 1.21.1** 的 Minecraft 模组开发项目。为保证代码质量、可维护性、安全性与可扩展性，请在开发过程中严格遵循以下规范。

## 一、基础环境与技术栈

- **工作目录**：`D:\Games\Minecraft\My Mods\Fintastic Supreme\fintastic_supreme-mc1.21.1-neoforge`
- **操作系统**：Windows 10
- **Java 版本**：JDK 21.0.2
- **构建工具**：Gradle (使用 `net.neoforged.moddev` 插件)
- **模组框架**：NeoForge 1.21.1
- **代码作者**：1 (Author: 1)
- **注释语言**：中文 (First Language)

### 目录结构规范

项目遵循标准的 NeoForge 模组结构，请严格保持以下目录树结构：

```text
fintastic_supreme-mc1.21.1-neoforge
├── .github/workflows          # CI/CD 工作流配置
├── gradle/wrapper             # Gradle Wrapper
├── src
│   ├── generated/resources      # 自动生成的资源文件 (数据生成器输出)
│   │   ├── assets/fintastic_supreme/models/item
│   │   └── data/fintastic_supreme/{advancement, recipe}
│   └── main
│       ├── java/com/chinaex123/fintastic_supreme  # 核心 Java 代码
│       │   ├── client           # 客户端专用逻辑 (渲染, 界面等)
│       │   ├── config           # 配置管理
│       │   ├── data             # 数据生成器 (Data Generators)
│       │   ├── event            # 事件监听与处理
│       │   │   └── fish_finder  # 特定模块事件 (如鱼群检测)
│       │   ├── init             # 注册表初始化 (Items, Blocks, Entities 等)
│       │   ├── mixin            # Mixin 注入代码
│       │   ├── network          # 网络通信 (Packets)
│       │   └── util             # 通用工具类
│       ├── resources            # 静态资源文件
│       │   └── assets/fintastic_supreme
│       │       ├── lang         # 本地化语言文件
│       │       └── textures     # 纹理资源
│       └── templates            # 元数据模板 (用于生成 mod.toml)
```

## 二、分层架构与代码组织

针对 Minecraft 模组开发的特性，采用以下分层与包结构规范：

| 包/层级 | 职责说明 | 开发约束与注意事项 |
|---------|----------|-------------------|
| **init** | 注册表初始化 | 集中注册 Items, Blocks, Entities, Sounds 等。避免在 init 中执行复杂逻辑，仅做注册。 |
| **event** | 事件处理 | 实现 `FMLCommonSetupEvent`, `ClientSetupEvent` 等。业务逻辑尽量解耦，复杂逻辑委托给其他类。 |
| **client** | 客户端逻辑 | 包含渲染器 (Renderers), 自定义屏幕 (Screens), 按键绑定等。**禁止**在此层访问服务器端数据或执行服务器端逻辑。 |
| **network** | 网络通信 | 定义数据包 (Packets)。确保数据包序列化/反序列化安全，防止恶意客户端注入。 |
| **data** | 数据生成 | 用于生成 JSON 文件 (Recipes, Loot Tables, Advancements)。保持生成逻辑与运行时逻辑分离。 |
| **config** | 配置管理 | 使用 NeoForge Config API 管理配置文件。提供默认值和热重载支持。 |
| **util** | 工具类 | 纯静态工具方法。避免状态持有，保持无副作用。 |
| **mixin** | Mixin 代码 | 用于注入 Minecraft 源码。必须添加 `@Mixin` 和 `@Unique` 等必要注解，避免命名冲突。 |

### 接口与实现分离

- 对于复杂的业务逻辑（如 `fish_finder` 模块），建议定义接口（如 `IFishFinderService`）并在 `impl` 包或同包下实现，便于测试和扩展。

## 三、安全与性能规范

### 输入校验与安全

- **网络包校验**：所有自定义网络数据包必须包含服务端校验逻辑，防止客户端发送非法数据导致崩溃或作弊。
- **资源加载**：避免在 `onItemUse` 或高频调用的事件中加载大型资源（如纹理、模型），应使用异步或预加载机制。
- **SQL 注入防范**：虽然 Minecraft 模组通常不使用 SQL，但若涉及外部数据库集成，严禁手动拼接 SQL 字符串，必须使用参数化查询。

### 事务与性能

- **主线程限制**：Minecraft 主线程（Render Thread 和 Server Thread）严禁执行耗时操作（如网络请求、复杂计算、文件 I/O）。必须使用 `ScheduledTask` 或 `CompletableFuture` 将耗时操作移至后台线程。
- **事件监听**：`@SubscribeEvent` 方法应尽可能轻量，避免阻塞主线程。复杂逻辑应通过发布事件或异步任务处理。

## 四、代码风格规范

### 命名规范

| 类型 | 命名方式 | 示例 |
|------|----------|------|
| 类名 | UpperCamelCase | `FishFinderEvent` |
| 方法/变量 | lowerCamelCase | `checkFishLocation()` |
| 常量 | UPPER_SNAKE_CASE | `MAX_FISH_COUNT` |
| 包名 | 全小写 | `com.chinaex123.fintastic_supreme.event` |

### 注释规范

- **语言要求**：所有类、方法、字段必须添加 **中文 Javadoc** 注释。
- **内容要求**：
  - 类注释需说明该类的主要职责。
  - 方法注释需说明参数含义、返回值及可能的异常。
  - 关键逻辑块需添加行内注释解释“为什么”这样做，而不仅仅是“做什么”。

### 实体与数据对象命名

| 后缀 | 用途说明 | 示例 |
|------|----------|------|
| Entity | Minecraft 实体类 | `FishEntity` |
| Block/Item | 方块/物品注册类 | `CustomFishItem` |
| Config | 配置类 | `ModConfig` |
| Packet | 网络数据包 | `SyncFishDataPacket` |

### 实体类简化工具

- **Lombok**：对于普通 Java 类（如 Config 数据类、工具类），可使用 Lombok 简化代码：
  - `@Data`
  - `@NoArgsConstructor`
  - `@AllArgsConstructor`
- **Minecraft 实体/方块**：**禁止**在 Entity 或 Block 类上使用 `@Data` 等 Lombok 注解，因为 Minecraft 的序列化机制和 Mixin 注入可能与之冲突。请手动编写 Getter/Setter 或使用 NeoForge 提供的辅助类。

## 五、扩展性与日志规范

### 接口优先原则

- 模块化设计：每个功能模块（如 `fish_finder`）应通过接口暴露能力，方便其他模组通过 Mixin 或 API 调用。

### 日志记录

- 使用 `@Slf4j` 注解（需引入 Lombok）或 `Logger` 实例进行日志记录。
- **日志级别**：
  - `INFO`：常规操作（如模组加载成功、配置加载）。
  - `DEBUG`：详细流程跟踪（仅用于开发调试，发布版本可关闭）。
  - `ERROR`：异常堆栈信息，必须包含上下文信息（如 Entity ID, 坐标等）。
  - **禁止**使用 `System.out.println`。

## 六、编码原则总结

| 原则 | 说明 |
|------|------|
| **SOLID** | 高内聚、低耦合，特别是在处理 Mixin 和事件监听时，确保单一职责。 |
| **DRY** | 避免重复代码，提取通用工具类到 `util` 包。 |
| **KISS** | 保持代码简洁易懂，避免过度设计。Minecraft 模组性能敏感，简单高效优先。 |
| **YAGNI** | 不实现当前不需要的功能，特别是未使用的注册表项或网络包。 |
| **OWASP** | 防范常见安全漏洞，如网络包注入、资源文件篡改等。 |
| **MC-Standard** | 遵循 Minecraft 模组开发最佳实践，如正确区分客户端/服务端逻辑，使用 NeoForge 提供的注册表机制。 |

## 七、构建与依赖管理

- **依赖来源**：主要依赖通过 CurseMaven 获取（如 `tide`, `cloth-config`, `jei`）。
- **本地依赖**：部分依赖（如 `jade`, `kubejs`）标记为 `localRuntime`，仅在运行时加载。
- **资源生成**：使用 `gradle data` 任务生成资源文件，确保 `src/generated/resources` 被正确包含在资源目录中。
- **元数据生成**：使用 `generateModMetadata` 任务从 `templates` 目录生成 `META-INF/mods.toml`。

---

*本规范基于 NeoForge 1.21.1 开发环境制定，请开发者在提交代码前自行检查是否符合上述规范。*
