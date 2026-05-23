# Prompt演化过程

- 初始Prompt：生成RealWorld SpringBoot+Vue项目
- 修改后Prompt：生成RealWorld SpringBoot+Vue脚手架，仅搭建基础架构，严禁写业务代码
- 修改原因：避免AI自动生成登录、文章等业务功能，保持项目为空白模板。

# Prompt演化过程

- 初始：阅读 <https://docs.realworld.show/introduction/，实现登陆注册功能>
- 文档站点无法访问，改用 WebSearch 搜索 "RealWorld Conduit API specification authentication login register endpoints"
- 探索项目结构使用 Explore Agent（并行）

# [Prompt演化过程]

- 初始 Prompt：实现 Home 功能（一句话，模糊）
- AI 第一次尝试：计划实现完整的文章/评论/收藏/关注全套功能，包括 favorites / follows 表、feed 接口、AuthRequired 鉴权等 —— 被用户中断并纠正
- 修改后 Prompt（精准限定）：
"严格依据 RealWorld 文档开发，只专注完成首页 Home 页面全套功能，用户中心、文章详情、发布文章、登录注册、评论、点赞、关注、个人主页、设置页等所有非首页功能一律禁止开发... 后端仅开发首页必需接口... 数据库仅创建首页依赖数据表... 不搭建登录鉴权逻辑..."

- 修改原因：AI 存在惯性"多做"倾向，看到项目有 Article/Comment/Profile 全套骨架，会自然地想去填充所有内容。需要用户在 Prompt 中明确定义"不做清单"（负向约束），并使用"禁止""一律不""剔除"等强否定词。

[Prompt演化过程]

- 初始Prompt : 阅读 <https://docs.realworld.show/introduction/> ,实现Profile页面和功能
- 无需修改，Prompt 清晰明确
