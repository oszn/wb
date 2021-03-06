## 假装weibo项目
<font size=5 color='red'>如果发现不小心泄露了密码请联系我，功能会慢慢加的</font>
v0.0.2
v0.0.3
v0.0.4
v0.0.6
### 注册(需要改成验证码验证邮箱是否有效还没做)
*新增昵称，还未修改邮箱的逻辑v0.0.3*
*完成验证码发送到邮箱才能继续注册v0.0.4*
http://39.96.45.191t:8080/register
3个参数账号密码邮箱。
如果账号存在就会提醒

![](img/1.png)

一个邮箱只能由一个账号如果重复了会提醒

![](img/2.png)

现在我把它后台账号先删掉再插入

![](img/3.png)

我突然发现这里逻辑有点问题，如果邮箱不存在那可能会造成可怕的后果，等会改改。

v0.0.4
首先会发送验证码到邮箱

![](img/25.png)

接受到邮件，然后在输入

![](img/26.png)

这就是输入的内容了

![](img/27.png)

### 登陆
http://39.96.45.191:8080/login
3种情况，账号不存在，密码错误，成功登陆

![](img/4.png)
![](img/5.png)
![](img/6.png)

### 修改密码（）
*需要讲验证码和修改的密码一起提交，方便前端v0.0.3*
*合并操作完成v.0.0.4*
这里修改密码分为3步。
第一步为邮箱验证，会随机生成一个5位数发送到邮箱去，然后返回一个账号的session到返回的请求
http://39.96.45.191:8080/change

![](img/7.png)
![](img/11.png)

可以看到多了一个session。然后多了一条邮件

![](img/10.png)
然后输入随机数
http://39.96.45.191:8080/yzm

![](img/12.png)

然后就是重置密码了。
http://39.96.45.191:8080/reset

![](img/13.png)

修改密码后登陆成功

![](img/14.png)

其实这里逻辑还是有点问题的。先不管了，先写着。目前就写了这么多，之后还有很多接口需要慢慢实现了。

### 发布weibo
*完成了把内容分词，并且上传到neo4j上面去,修复了没有上传图片的导致错误，增加了可以上传多张图片v.0.0.4*
http://39.96.45.191:8080/upload/weibo
这一块是比较核心的内容了，发布微博需要存储几个东西，文章和图片和时间3个东西。然后数据库储存一个拥有者和一个文章id，这个很重要。
其实这一块是设计时间由我后端来发，还是前端提供了，最后我想了像还是前端给吧。虽然很不合理，如果有些人改成了一些奇怪的时间，就会很不合理，这个以后在沟通吧，比较接口写的很松散，只需要改下入口就行了。
整体的设计就是

![](img/15.png)

接下来复现下过程。对了这个整个之前需要先登录，搞一个cookie才行。登陆玩之后的流程如下

![](img/16.png)

图片哪里是

![](img/17.png)

然后数据库

![](img/18.png)
![](img/19.png)

图片这一块就是先保存临时文件然后通过一个请求发送到图片服务器上面。
然后访问图片链接
这整个过程就在我帅气的照片种完成吧。

![](img/20.png)

v0.0.4新增模块
先通过分词器把文章先分词，只收录名词然后插入数据库
可以看了这样文章内提到老师的文章有

![](img/23.png)

也可以通过联合搜索来查找2个关键词都有的文章
你看180这篇文章，不知道啥东西反正就很重口味，我们现在研究还不升入，如果之后能分解关系就更好了。
这个就是我们搜索1.0很基础，但是还是很实用。虽然搜索效果肯定没有用分解关系来的好。
这里的提取会返回很多文章id，也会有重复的文章id，所以先用map储存下来，然后排序返回次数最多的文章。

![](img/24.png)

### 评论和文章的点赞
这个过程就是根据评论或者文章的id来进行连接的，发评论我还没写，但是点赞付出写了，不过没办法测试。
点赞的过程通过mysql的储存过程完成的。
```sql
drop PROCEDURE insc;
DELIMITER $$ 
create PROCEDURE insc(in t int)
BEGIN
DECLARE p int(11) DEFAULT 0;
select CommentLikeCount into p from WeiboComment where CommentId=t;
UPDATE WeiboComment set CommentLikeCount=p+1  where CommentId=wid;
END$$
DELIMITER ;
```
![](img/22.png)
这个就是先查找喜欢的次数然后update+1.这个过程写的比较糙，没有返回。
http://39.96.45.191:8080/like/passage
![](img/21.png)
可以看到43的点赞多了5个、。

### 返回json v0.0.5
这个板块是通过mybaits完成的，讲文章评论都放在对应的类里面，然后进行一个json的层层拼接返回即可。
这里是设置3个类，人物类，文章类和评论类，然后在一个函数种生成json。不过我刚刚得到噩耗，cookie好像在前后端分类上有点问题，裂开。这个的输入就是cookie。大烈。
![](img/28.png)

### 搜索 v0.0.5
在搜索这一块需要做缓存了，因为一直去数据库找不是很好，我们决定用redis。这一块我构思了一会发现情况不太对劲，感觉很离谱，之后在讨论构思出一个好一点的架构吧。这一块架构很重要，如果处理不当，会造成很差的用户体验。

## v0.0.4说明
修改了注册的逻辑需要发送验证码，然后才能注册（需要前端设置不能验证码发送太频繁，因为后端没有设置时钟事件）
把找回密码进行整合
修复了weibo上传图片为空的情况，也增加了可以上传多个图片的内容。
正在开发搜索功能的雏形。

## v0.0.5说明
现在好像碰到了比较难的一块了，就是在设置策略上面不是很了解，还需要多看看其他人如何处理在这个方面。

## v0.0.6说明
增加了上传头像，修改了返回weibo，返回weibo粉丝，被关注，微博数目信息
### 上传头像
http://127.0.0.1:8080/upload/headImg
![](img/29.png)
需要上传时间(time)和图片(files)

### 返回weibo
返回如下如果没有图片则为null
```json
{
    "toId": -1,
    "myHeader": null,
    "btnShow": false,
    "MyName": "yy",
    "myId": "100086",
    "replyComment": "",
    "index": "0",
    "comment": [
        {
            "commentNum": 0,
            "img": [],
            "headImg": null,
            "MyName": "yy",
            "like": 0,
            "weiboId": "61",
            "comment": "最近，朱婷受到了不公正的待遇。中国女排代言的某火腿肠商家发了一个短视频，视频中他们对于包装上朱婷的肖像进行了多种方式的遮挡，引发了巨大争议，不少球迷认为这是厂家对朱婷的不尊重。让球迷们气愤的是，这个商家在微博上居然采用了指桑骂槐的做法，发布谐音猪皮，更引起了球迷们的怒火。",
            "inputShow": false,
            "id": "100086",
            "time": "2020-05-23 11:43:13.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [],
            "headImg": null,
            "MyName": "yy",
            "like": 0,
            "weiboId": "62",
            "comment": "最近，朱婷受到了不公正的待遇。中国女排代言的某火腿肠商家发了一个短视频，视频中他们对于包装上朱婷的肖像进行了多种方式的遮挡，引发了巨大争议，不少球迷认为这是厂家对朱婷的不尊重。让球迷们气愤的是，这个商家在微博上居然采用了指桑骂槐的做法，发布谐音猪皮，更引起了球迷们的怒火。",
            "inputShow": false,
            "id": "100086",
            "time": "2020-05-20 11:43:13.0",
            "reply": []
        }
    ],
    "to": ""
}

```
大概如上
接下来是返回weibo得关注粉丝和weibo数目
### weibo关注粉丝等
![](img/30.png)
返回结构如下
```json
{
    "weiboCount": 2,
    "attention": 2,
    "fans": 1
}

```
Mysql(mysql)改了几个地方，从

![](img/31.png)
这里以后全是新加的

![](img/32.png)
这个地方改了

## v0.0.6补充
http://127.0.0.1:8080/respone/passage
修改了返回自己weibo的json数据的一些属性
如下
<details>
<summary>json data</summary>

```json
{
    "toId": -1,
    "myHeader": null,
    "btnShow": false,
    "MyName": "yy",
    "myId": "100086",
    "replyComment": "",
    "index": "0",
    "comment": [
        {
            "commentNum": 0,
            "img": [],
            "headImg": null,
            "MyName": "yy",
            "like": 0,
            "weiboId": "61",
            "comment": "最近，朱婷受到了不公正的待遇。中国女排代言的某火腿肠商家发了一个短视频，视频中他们对于包装上朱婷的肖像进行了多种方式的遮挡，引发了巨大争议，不少球迷认为这是厂家对朱婷的不尊重。让球迷们气愤的是，这个商家在微博上居然采用了指桑骂槐的做法，发布谐音猪皮，更引起了球迷们的怒火。",
            "inputShow": false,
            "id": "100086",
            "time": "2020-05-23 11:43:13.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [],
            "headImg": null,
            "MyName": "yy",
            "like": 0,
            "weiboId": "62",
            "comment": "最近，朱婷受到了不公正的待遇。中国女排代言的某火腿肠商家发了一个短视频，视频中他们对于包装上朱婷的肖像进行了多种方式的遮挡，引发了巨大争议，不少球迷认为这是厂家对朱婷的不尊重。让球迷们气愤的是，这个商家在微博上居然采用了指桑骂槐的做法，发布谐音猪皮，更引起了球迷们的怒火。",
            "inputShow": false,
            "id": "100086",
            "time": "2020-05-20 11:43:13.0",
            "reply": []
        }
    ],
    "to": ""
}

```
</details>

### 返回好友weibo接口
http://127.0.0.1:8080/response/friend

<details><summary>json data</summary>

```json
{
    "toId": -1,
    "myHeader": null,
    "btnShow": false,
    "MyName": "yy",
    "myId": "100086",
    "replyComment": "",
    "index": "0",
    "comment": [
        {
            "commentNum": 1,
            "img": [
                "http://localhost/files/photo/15909325230.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 2,
            "weiboId": "108",
            "comment": "晚上好",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-31 21:42:03.0",
            "reply": [
                {
                    "toId": 1091756452,
                    "commentNum": 0,
                    "headImg": "http://localhost/files/photo/1590932913.jpg",
                    "like": 1,
                    "commentId": 18,
                    "comment": "5656",
                    "inputShow": false,
                    "from": "帅气啊刘",
                    "time": "2020-05-31 23:46:45.0",
                    "to": "帅气啊刘",
                    "fromId": 1091756452
                }
            ]
        },
        {
            "commentNum": 0,
            "img": [],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "47",
            "comment": "女权出击",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-23 11:43:11.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "60",
            "comment": "最近，朱婷受到了不公正的待遇。中国女排代言的某火腿肠商家发了一个短视频，视频中他们对于包装上朱婷的肖像进行了多种方式的遮挡，引发了巨大争议，不少球迷认为这是厂家对朱婷的不尊重。让球迷们气愤的是，这个商家在微博上居然采用了指桑骂槐的做法，发布谐音猪皮，更引起了球迷们的怒火。",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-23 11:43:11.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "59",
            "comment": "最近，朱婷受到了不公正的待遇。中国女排代言的某火腿肠商家发了一个短视频，视频中他们对于包装上朱婷的肖像进行了多种方式的遮挡，引发了巨大争议，不少球迷认为这是厂家对朱婷的不尊重。让球迷们气愤的是，这个商家在微博上居然采用了指桑骂槐的做法，发布谐音猪皮，更引起了球迷们的怒火。",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-23 11:43:11.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15902053910.png",
                "http://localhost/files/photo/15902053910.png"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "54",
            "comment": "最近，朱婷受到了不公正的待遇。中国女排代言的某火腿肠商家发了一个短视频，视频中他们对于包装上朱婷的肖像进行了多种方式的遮挡，引发了巨大争议，不少球迷认为这是厂家对朱婷的不尊重。让球迷们气愤的是，这个商家在微博上居然采用了指桑骂槐的做法，发布谐音猪皮，更引起了球迷们的怒火。",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-23 11:43:11.0",
            "reply": []
        },
        {
            "commentNum": 8,
            "img": [],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 6,
            "weiboId": "53",
            "comment": "中国女排队长朱婷是非常受球迷们喜爱的一位球员，朱婷作为中国女排的头号球星已经带领女排国家队取得了三次世界大赛的冠军，在欧洲职业联赛中，朱婷也取得了无比辉煌的成就。目前的朱婷可谓是中国排坛的一姐，作为中国体坛少有的超级巨星，朱婷在球迷们心目之中具备很高的地位。",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-23 11:43:11.0",
            "reply": [
                {
                    "toId": 1,
                    "commentNum": 0,
                    "headImg": "http://localhost/files/photo/1590932913.jpg",
                    "like": 13,
                    "commentId": 2,
                    "comment": "你的18n了？",
                    "inputShow": false,
                    "from": "帅气啊刘",
                    "time": null,
                    "to": "ami",
                    "fromId": 1091756452
                },
                {
                    "toId": 1091756452,
                    "commentNum": 1,
                    "headImg": null,
                    "like": 2,
                    "commentId": 3,
                    "comment": "你的18n了？",
                    "inputShow": false,
                    "from": "ami",
                    "time": null,
                    "to": "帅气啊刘",
                    "fromId": 1
                },
                {
                    "toId": 1,
                    "commentNum": 2,
                    "headImg": null,
                    "like": 6,
                    "commentId": 4,
                    "comment": "就你这5n？",
                    "inputShow": false,
                    "from": "ami",
                    "time": null,
                    "to": "ami",
                    "fromId": 2
                },
                {
                    "toId": 2,
                    "commentNum": 3,
                    "headImg": null,
                    "like": 0,
                    "commentId": 5,
                    "comment": "别bb，你是傻子",
                    "inputShow": false,
                    "from": "ami",
                    "time": null,
                    "to": "ami",
                    "fromId": 3
                },
                {
                    "toId": 3,
                    "commentNum": 4,
                    "headImg": null,
                    "like": 0,
                    "commentId": 6,
                    "comment": "???????",
                    "inputShow": false,
                    "from": "ami",
                    "time": null,
                    "to": "ami",
                    "fromId": 1
                },
                {
                    "toId": 1091756452,
                    "commentNum": 5,
                    "headImg": "http://localhost/files/photo/1590932913.jpg",
                    "like": 0,
                    "commentId": 7,
                    "comment": "",
                    "inputShow": false,
                    "from": "帅气啊刘",
                    "time": "2020-05-31 11:05:10.0",
                    "to": "帅气啊刘",
                    "fromId": 1091756452
                },
                {
                    "toId": 1,
                    "commentNum": 6,
                    "headImg": "http://localhost/files/photo/1590932913.jpg",
                    "like": 0,
                    "commentId": 11,
                    "comment": "。。。。。",
                    "inputShow": false,
                    "from": "帅气啊刘",
                    "time": "2020-05-31 16:09:34.0",
                    "to": "ami",
                    "fromId": 1091756452
                },
                {
                    "toId": 1091756452,
                    "commentNum": 7,
                    "headImg": "http://localhost/files/photo/1590932913.jpg",
                    "like": 0,
                    "commentId": 14,
                    "comment": "晚上好",
                    "inputShow": false,
                    "from": "帅气啊刘",
                    "time": "2020-05-31 21:49:37.0",
                    "to": "帅气啊刘",
                    "fromId": 1091756452
                }
            ]
        },
        {
            "commentNum": 0,
            "img": [],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "51",
            "comment": "女权出击",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-23 11:43:11.0",
            "reply": []
        },
        {
            "commentNum": 7,
            "img": [
                "http://localhost/files/photo/15902053910.png",
                "http://localhost/files/photo/15902053911.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 69,
            "weiboId": "46",
            "comment": "女权出击",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-23 11:43:11.0",
            "reply": [
                {
                    "toId": 1091756452,
                    "commentNum": 0,
                    "headImg": "http://localhost/files/photo/1590932913.jpg",
                    "like": 0,
                    "commentId": 8,
                    "comment": "狠人",
                    "inputShow": false,
                    "from": "帅气啊刘",
                    "time": "2020-05-31 14:40:15.0",
                    "to": "帅气啊刘",
                    "fromId": 1091756452
                },
                {
                    "toId": 1091756452,
                    "commentNum": 1,
                    "headImg": "http://localhost/files/photo/1590932913.jpg",
                    "like": 0,
                    "commentId": 9,
                    "comment": "。。。。。",
                    "inputShow": false,
                    "from": "帅气啊刘",
                    "time": "2020-05-31 15:50:38.0",
                    "to": "帅气啊刘",
                    "fromId": 1091756452
                },
                {
                    "toId": 1091756452,
                    "commentNum": 2,
                    "headImg": "http://localhost/files/photo/1590932913.jpg",
                    "like": 0,
                    "commentId": 10,
                    "comment": "？？？？",
                    "inputShow": false,
                    "from": "帅气啊刘",
                    "time": "2020-05-31 16:07:57.0",
                    "to": "帅气啊刘",
                    "fromId": 1091756452
                },
                {
                    "toId": 1091756452,
                    "commentNum": 3,
                    "headImg": "http://localhost/files/photo/1590932913.jpg",
                    "like": 0,
                    "commentId": 12,
                    "comment": "。。。",
                    "inputShow": false,
                    "from": "帅气啊刘",
                    "time": "2020-05-31 21:48:58.0",
                    "to": "帅气啊刘",
                    "fromId": 1091756452
                },
                {
                    "toId": 1091756452,
                    "commentNum": 4,
                    "headImg": "http://localhost/files/photo/1590932913.jpg",
                    "like": 0,
                    "commentId": 13,
                    "comment": "为什么",
                    "inputShow": false,
                    "from": "帅气啊刘",
                    "time": "2020-05-31 21:49:21.0",
                    "to": "帅气啊刘",
                    "fromId": 1091756452
                },
                {
                    "toId": 1091756452,
                    "commentNum": 5,
                    "headImg": "http://localhost/files/photo/1590932913.jpg",
                    "like": 0,
                    "commentId": 16,
                    "comment": "打发",
                    "inputShow": false,
                    "from": "帅气啊刘",
                    "time": "2020-05-31 23:01:30.0",
                    "to": "帅气啊刘",
                    "fromId": 1091756452
                },
                {
                    "toId": 1091756452,
                    "commentNum": 6,
                    "headImg": "http://localhost/files/photo/1590932913.jpg",
                    "like": 0,
                    "commentId": 17,
                    "comment": "打发2",
                    "inputShow": false,
                    "from": "帅气啊刘",
                    "time": "2020-05-31 23:02:25.0",
                    "to": "帅气啊刘",
                    "fromId": 1091756452
                }
            ]
        },
        {
            "commentNum": 0,
            "img": [],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "48",
            "comment": "女权出击",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-23 11:43:11.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "49",
            "comment": "女权出击",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-23 11:43:11.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "52",
            "comment": "上一轮，柏林赫塔客场3-0战胜霍芬海姆，现在4-0大胜柏林联合，两场全胜+净胜7球。\n\n克林斯曼下课后，54岁德国教练拉巴迪亚接班执起柏林赫塔的教鞭，前两场德甲比赛就给球迷带来了巨大的惊喜。",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-23 11:43:11.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "50",
            "comment": "女权出击",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-23 11:43:11.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "85",
            "comment": "645645",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "83",
            "comment": "123465",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "86",
            "comment": "4465455545",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "87",
            "comment": "4465455545hhh",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "88",
            "comment": "最后一次",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "89",
            "comment": "最后一法法次",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "90",
            "comment": "。。。。。。。",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "91",
            "comment": "456456",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "92",
            "comment": "44",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "93",
            "comment": "45",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "94",
            "comment": "46",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "95",
            "comment": "47",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "96",
            "comment": "48",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "97",
            "comment": "49",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "98",
            "comment": "50",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "99",
            "comment": "51",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "100",
            "comment": "52",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "101",
            "comment": "53",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "102",
            "comment": "54",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "103",
            "comment": "55",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "104",
            "comment": "56",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "105",
            "comment": "57",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "106",
            "comment": "58",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "107",
            "comment": "59",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg",
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "72",
            "comment": "9",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "57",
            "comment": "第三次测试",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 12,
            "weiboId": "58",
            "comment": "测试5",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "55",
            "comment": "测试",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg",
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "64",
            "comment": "123456",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "65",
            "comment": "。。。。",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "66",
            "comment": "有毒",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "67",
            "comment": "垃圾upload",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "68",
            "comment": "好烦啊，。。。",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "69",
            "comment": "？？？",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "70",
            "comment": "搞什么搞",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "71",
            "comment": "123789",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "84",
            "comment": "666123465",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "73",
            "comment": "9",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "74",
            "comment": "56556",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "75",
            "comment": "123",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "76",
            "comment": "要疯了",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "77",
            "comment": "开始烦躁",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "78",
            "comment": "这是第几次",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "79",
            "comment": "第31次",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "80",
            "comment": "123456",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "81",
            "comment": "12356",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15900768000.jpg",
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "82",
            "comment": "123465",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": []
        },
        {
            "commentNum": 1,
            "img": [
                "http://localhost/files/photo/15900768000.jpg"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 1,
            "weiboId": "56",
            "comment": "第二次测试",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-22 00:00:00.0",
            "reply": [
                {
                    "toId": 1091756452,
                    "commentNum": 0,
                    "headImg": "http://localhost/files/photo/1590932913.jpg",
                    "like": 0,
                    "commentId": 15,
                    "comment": "服了",
                    "inputShow": false,
                    "from": "帅气啊刘",
                    "time": "2020-05-31 21:49:54.0",
                    "to": "帅气啊刘",
                    "fromId": 1091756452
                }
            ]
        },
        {
            "commentNum": 0,
            "img": [],
            "headImg": "",
            "MyName": "ami",
            "like": 0,
            "weiboId": "63",
            "comment": "最近，朱婷受到了不公正的待遇。中国女排代言的某火腿肠商家发了一个短视频，视频中他们对于包装上朱婷的肖像进行了多种方式的遮挡，引发了巨大争议，不少球迷认为这是厂家对朱婷的不尊重。让球迷们气愤的是，这个商家在微博上居然采用了指桑骂槐的做法，发布谐音猪皮，更引起了球迷们的怒火。",
            "inputShow": false,
            "id": "1",
            "time": "2020-05-21 11:43:13.0",
            "reply": []
        }
    ],
    "to": ""
}
```
</details>

## v0.0.7
此次更新加了搜索功能

### 搜索
http://127.0.0.1:8080/search
输入是cookie+搜索的词条。
例如输入朱婷得到是

<details>
<summary>json data</summary>

```json
{
    "toId": -1,
    "myHeader": null,
    "btnShow": false,
    "MyName": "yy",
    "myId": "100086",
    "replyComment": "",
    "index": "0",
    "comment": [
        {
            "commentNum": 0,
            "img": [
                "http://localhost/files/photo/15902053910.png",
                "http://localhost/files/photo/15902053910.png"
            ],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 0,
            "weiboId": "54",
            "comment": "最近，朱婷受到了不公正的待遇。中国女排代言的某火腿肠商家发了一个短视频，视频中他们对于包装上朱婷的肖像进行了多种方式的遮挡，引发了巨大争议，不少球迷认为这是厂家对朱婷的不尊重。让球迷们气愤的是，这个商家在微博上居然采用了指桑骂槐的做法，发布谐音猪皮，更引起了球迷们的怒火。",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-23 11:43:11.0",
            "reply": []
        },
        {
            "commentNum": 8,
            "img": [],
            "headImg": "http://localhost/files/photo/1590932913.jpg",
            "MyName": "帅气啊刘",
            "like": 6,
            "weiboId": "53",
            "comment": "中国女排队长朱婷是非常受球迷们喜爱的一位球员，朱婷作为中国女排的头号球星已经带领女排国家队取得了三次世界大赛的冠军，在欧洲职业联赛中，朱婷也取得了无比辉煌的成就。目前的朱婷可谓是中国排坛的一姐，作为中国体坛少有的超级巨星，朱婷在球迷们心目之中具备很高的地位。",
            "inputShow": false,
            "id": "1091756452",
            "time": "2020-05-23 11:43:11.0",
            "reply": [
                {
                    "toId": 1,
                    "commentNum": 0,
                    "headImg": "http://localhost/files/photo/1590932913.jpg",
                    "like": 13,
                    "commentId": 2,
                    "comment": "你的18n了？",
                    "inputShow": false,
                    "from": "帅气啊刘",
                    "time": null,
                    "to": "ami",
                    "fromId": 1091756452
                },
                {
                    "toId": 1091756452,
                    "commentNum": 1,
                    "headImg": null,
                    "like": 2,
                    "commentId": 3,
                    "comment": "你的18n了？",
                    "inputShow": false,
                    "from": "ami",
                    "time": null,
                    "to": "帅气啊刘",
                    "fromId": 1
                },
                {
                    "toId": 1,
                    "commentNum": 2,
                    "headImg": null,
                    "like": 6,
                    "commentId": 4,
                    "comment": "就你这5n？",
                    "inputShow": false,
                    "from": "ami",
                    "time": null,
                    "to": "ami",
                    "fromId": 2
                },
                {
                    "toId": 2,
                    "commentNum": 3,
                    "headImg": null,
                    "like": 0,
                    "commentId": 5,
                    "comment": "别bb，你是傻子",
                    "inputShow": false,
                    "from": "ami",
                    "time": null,
                    "to": "ami",
                    "fromId": 3
                },
                {
                    "toId": 3,
                    "commentNum": 4,
                    "headImg": null,
                    "like": 0,
                    "commentId": 6,
                    "comment": "???????",
                    "inputShow": false,
                    "from": "ami",
                    "time": null,
                    "to": "ami",
                    "fromId": 1
                },
                {
                    "toId": 1091756452,
                    "commentNum": 5,
                    "headImg": "http://localhost/files/photo/1590932913.jpg",
                    "like": 0,
                    "commentId": 7,
                    "comment": "",
                    "inputShow": false,
                    "from": "帅气啊刘",
                    "time": "2020-05-31 11:05:10.0",
                    "to": "帅气啊刘",
                    "fromId": 1091756452
                },
                {
                    "toId": 1,
                    "commentNum": 6,
                    "headImg": "http://localhost/files/photo/1590932913.jpg",
                    "like": 0,
                    "commentId": 11,
                    "comment": "。。。。。",
                    "inputShow": false,
                    "from": "帅气啊刘",
                    "time": "2020-05-31 16:09:34.0",
                    "to": "ami",
                    "fromId": 1091756452
                },
                {
                    "toId": 1091756452,
                    "commentNum": 7,
                    "headImg": "http://localhost/files/photo/1590932913.jpg",
                    "like": 0,
                    "commentId": 14,
                    "comment": "晚上好",
                    "inputShow": false,
                    "from": "帅气啊刘",
                    "time": "2020-05-31 21:49:37.0",
                    "to": "帅气啊刘",
                    "fromId": 1091756452
                }
            ]
        },
        {
            "commentNum": 0,
            "img": [],
            "headImg": "",
            "MyName": "yy",
            "like": 0,
            "weiboId": "61",
            "comment": "最近，朱婷受到了不公正的待遇。中国女排代言的某火腿肠商家发了一个短视频，视频中他们对于包装上朱婷的肖像进行了多种方式的遮挡，引发了巨大争议，不少球迷认为这是厂家对朱婷的不尊重。让球迷们气愤的是，这个商家在微博上居然采用了指桑骂槐的做法，发布谐音猪皮，更引起了球迷们的怒火。",
            "inputShow": false,
            "id": "100086",
            "time": "2020-05-23 11:43:13.0",
            "reply": []
        },
        {
            "commentNum": 0,
            "img": [],
            "headImg": "",
            "MyName": "ami",
            "like": 0,
            "weiboId": "63",
            "comment": "最近，朱婷受到了不公正的待遇。中国女排代言的某火腿肠商家发了一个短视频，视频中他们对于包装上朱婷的肖像进行了多种方式的遮挡，引发了巨大争议，不少球迷认为这是厂家对朱婷的不尊重。让球迷们气愤的是，这个商家在微博上居然采用了指桑骂槐的做法，发布谐音猪皮，更引起了球迷们的怒火。",
            "inputShow": false,
            "id": "1",
            "time": "2020-05-21 11:43:13.0",
            "reply": []
        }
    ],
    "to": "",
    "search_user": [],
    "search_id": []
}
```
</details>

可以看到这个相比于之前多了一个search_user和search_id
是怎样的了，如下。
例如输入“帅”他就会出现如下

```json

{
    "myHeader": "http://localhost/files/photo/1590932913.jpg",
    "btnShow": false,
    "comments": [],
    "myId": "100086",
    "myName": "yy",
    "index": "0",
    "search_user": [
        {
            "name": "帅气啊刘",
            "id": "1091756452"
        }
    ],
    "search_id": []
}

```

search_user就出现了与帅相关的。
再比如我输入一个账号

```json

{
    "myHeader": "http://localhost/files/photo/1590932913.jpg",
    "btnShow": false,
    "comments": [],
    "myId": "100086",
    "myName": "yy",
    "index": "0",
    "search_user": [],
    "search_id": [
        {
            "name": "帅气啊刘",
            "id": "1091756452"
        }
    ]
}

```

就像这样，当然如果有那种都符合的都会返回，不过值得注意的是，这个程序做的测试不多，可能存在bug。目前为止存在最大的bug就是mysql链接超时就会断掉的问题。


目前为止这是搜索功能，热搜功能不知道写不写，写的话就要涉及到redis了，因为时效性，思路是储存搜索词条和搜索文章，然后没次搜索count++然后这么处理，不过中间有太多的问题了，重复度问题，词语相似度问题，这个很难解决。举个例子OVER WATCH你可以说是守望先锋，不过大多人喜欢叫这个游戏为屁股。那么你对于一个相同的词条有3中解释，其中的一种解释在存在歧义，那做起来必须使用到数据量巨tm大的知识图谱了，这样才能推理分析。

此上就是v0.0.7所做的工作。接下来一步就是推荐了，推荐我们把所有的短文本分为15类。

## v0.0.7suply
此次更新修复了一些效率问题，由于sql执行次数太多了，导致整体效率很底，所以将需要查询的内容放入了内存之中，所以效率搞了很多。
 
目前文章分类的代码已经部署完毕，效果有待考察，先随便演示。
文化/娱乐/体育/财经/房产/汽车/教育/科技/军事/旅游/国际/证券/农业/电竞/民生
![](img/35.png)
![](img/36.png)
![](img/37.png)
![](img/38.png)

可以看出还是有效果，但是不一定很行。

## v0.0.8
此次更新添加了返回关注列表和返回列表，已经取消关注等功能，修改了了用户返回图片的情况。
http://127.0.0.1:8080/list/fans
![](img/39.png)

http://127.0.0.1:8080/list/attention 
![](img/40.png)

http://127.0.0.1:8080/cancel/link
取消关注输入是id
如下
![](img/41.png)
![](img/42.png)

再看看我们的数据里面
![](img/43.png)
关于1的link关系就没了，不过他的节点还是在里面。这个时候我们在关注下。

![](img/44.png)
不过不得不说，当时这个接口写的太紧了，没有验证码无法添加节点，所以可能之后需要把这个地方改掉，改成一个条件语句，好插入一些虚拟用户。

其实这里存在一些问题，你看见了这么多用户，但是在mysql数据库里面只有3个用户，所以只有通过正当的接口才能同事操作2个数据库。感觉怪怪的。

接下来，可能需要做的就是推荐了，推荐搞完了，应该就差不多了。

## v0.0.9
此次更新多添加了一条互相关注的字段，both_attenion，如下测试

![](img/45.png)

这个0表示互相关注,1表示关注，2表示被关注，3表示没关系

新增一个点击头像返回信息的。

http://127.0.0.1:8080/friend/info
输入的参数是id也就是别人的id返回如下。

![](img/46.png)

```json

{
    "headImg": "http://localhost/files/photo/1590932913.jpg",
    "name": "帅气啊刘",
    "id": "1091756452",
    "relationship": 0,
    "introduction": "我是骚气刘意"
}
```

当然可以看出来前面返回的都是没有introduction。不过这个在get_user_info这个函数里面已经放回返回值了，只需要在下一个接口放到里面就可以，这个还需要和组长商量，先留着。

### 下一步
目前存在的问题就是搜索太慢了，需要优化，我表示不想优化，
现在在做文本推荐，目前15个类已经完成。
现在展现下效果。

首先是15个类

![](img/47.png)

这个可以看上面，可以看到那15个类。

这个时候我们打开游戏

![](img/48.png)

这里我提取了前1000子关键词。对呀fw能够被筛选出来，每一个祖安人都有责任，hhhh。可惜的是今天uzi刚宣布了退役，爷青结。所以接下以uzi展开。

![](img/49.png)
当你把uzi点开，你会看见紫色的是写他文章，熟悉的4包1，懂得都懂好吧。

这个时候我点开了其中一个英雄联盟在+点开一个英雄，结果内存就炸了，这玩意可视化，太费内存了

![](img/50.png)

当然做到这一步了，推荐算法已经出来了，基于兴趣点的推荐，写简单点好，太复杂我也不会写md。

至于怎么分析的这个代码，我今天要去打游戏了，就先不发了，明天先整理下。

## v0.1.1
ohhhhhhhhhhhhh!!!!!!!!!!!!!!!!终于要完结了
此次后台添加了热搜和推荐功能。
### 热搜
http://127.0.0.1:8080/hot
无输入
热搜做的比较简单，就用redis来操作。没有做些处理，直接用搜索词作为key，然后次数作为count来。然后设置一个schedule事件，每天0点就清楚掉热搜。重新计算。这个处理就是这么简单，来看看效果。

![](img/51.png)

### 推荐
http://127.0.0.1:8080/response/recommend
无输入
为了写推荐，感觉重构的很多地方，比如把整个neo4j的数据库从我的服务器转移到csh的服务器上面去了。当然我建了很多假用户。这个不影响。这个推荐其实只写一个部分，就是兴趣点推荐。新建一个一张搜索记录表，用来储存搜索记录，然后把下一步可以吧关键词记录下来，不过很麻烦，我就没弄了。

改的地方主要在上传和搜索部分。上传部分，现在每次上传都会进行一个分类，得到文本的类型，然后放入数据库和neo4j。之前上传的，我就随便标记了下。他上传之后，会对文本分词，然后放入neo4j，然后会对关键词记录，对关键词进行关联，这里注意的是关键词已经预处理放入redis里面了，因为neo4j太慢了，所以放在redis里面。看下上传后neo4j的效果吧。刚刚出了意外，调试了一会，现在我们换个账号3号登陆下，看看插入的效果。
![](img/53.png)
这张图就很纯粹简单，有一个874号用户关注了3号，然后3号写了文章132.文章132有2个提取词，队和赛区。然后这个又符合游戏的关键字赛区。然后赛区展开，发现属于电竞这个行列，然后发现文章131和文章129也是有着赛区这个关键字。所以这就形成一个很好的关系网络，目前我没办法去做这个的分析，主要原因是时间不太行。

了解完这些后，我们可以吧一个作者写过的东西的分类进行一个分析。所以得到最多分类的前三名，然后同样对搜索也是一个策略，返回搜索记录的前3，当然搜索记录仅限于前20天内的。这样根据这些类别，然后返回靠时间最近的前10名，这样不断地刷新，可以保证热搜。当然这样写推荐模块还是很简单不合理，有些既没有发文章也没有搜索，那就无了。所以之后如果我要写，肯定会加一个随机性站0.1的比列返回，好友站0.3，其他的站0.6.当然我还没写，妈的，我太难了，写了好几个小时这个部分。
这个版块还是有问题，我没办法做到实时更新，所以设置了一个schedule任务，每4个小时更新一次，用户的搜索和写作的兴趣点，当然这个多少小时更新一次和用户数目肯定有关系，如果用户贼多，你可能还没更新完，就进入了下一次更新，这个时间的设置可以计算下复杂度，来调控。
如果想做到实时更新，我觉得我最多放0.05的版块给他，因为他这个太耗费时间了，这个我觉得我来做，我肯定会引入拓扑，针对是每次浏览的拓扑，这样比较快，这个肯定还是需要与处理的。我觉得一个可行的理论是，随机中不随机，也就是一定限度的随机，然后用户点击了，就留下来可能性大，这种末尾淘汰制，要远比计算整个板块来的简洁和快，当然我比不会这么做，因为没那个场景。

正如之前所提到的，他必须4小时更新一次，我正准备测试下我的推荐功能，发现没有结果，看来要么等4小时，要么重启一次，然后在等几分钟他把内容写到数据。这个就先不展示了。