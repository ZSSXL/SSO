# SSO - 单点登录的学习与实现
**Author:** ZSS

**CreateTime:** 2020-11-19

---

- 首先根据自己的理解，自己实现一个

  经过了几天的努力和尝试，完整了一个可以实现二级域名之间的单点登录。**完全不同域名的单点登录现在还不能解决**。

整个过程绕来绕去，下面就梳理一下整个过程。

----

搭建了三个项目: certification-center（用户认证中心），application-one（应用程序 - 1），application-two（应用程序 - 2）

1. **certification-center**

   	这个用户认证中心主要就是用来存储用户信息，提供用户注册、登录、校验等接口

2. **application（one & two）**

​	   实现某些业务功能，有自己的功能接口，但是用户调用该接口首先都得通过用户认证中心对用户的登录状态和身份进行校验，成功则进行则继续执行后面的。

---

> 整个流程如下

首先application发送功能请求，通过接口拦截，首先访问cetification-center的用户校验接口，发现用户未登录，接口请求失败。页面跳转至application的登录界面（该界面调用的用户登录的接口为用户认证中心提供），登录成功后接口返回用户凭证**ticket**，application在前端通过`document.cookies="sso_ticket=result.data;path=/;"`，将ticket存入`Cookies`中。每次请求都需从`Cookies`中获取用户凭证，将其通过`requestHeader`传给后端获取，进行用户校验。

#### 注：应用每次请求，都需要从`Cookies`中获取对应的cookie。



> 总结：

目前可以实现二级域名下的单点登录，也就是a.ccc.com和b.ccc.com之间可以实现单点登录，但是完全不同域名就不行了，让我很是苦恼，想不出有什么比较好的解决办法，后面再想想办法。

---

### 主要代码展示

1. 用户登录

```java
@PostMapping("/login")
public ServerResponse<String> login(@RequestBody LoginDTO login,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
    // 1. 校验用户
    UserDTO userDTO = userService.checkUser(login);
    // 2. 生成ticket or token
    String ticket = request.getSession().getId();
    // 3. 保存用户信息SessionGeneralConverter
    String userSerializable = SerializableUtil.serializable(userDTO);
    // 4. 设置Cookie，其实这个不需要，用不到
    Cookie cookie = new Cookie("sso_token", ticket);
    cookie.setPath("/");
    response.addCookie(cookie);

    String setResult = redisUtil.set(ticket, userSerializable, Constant.EFFECTIVE_TIME);
    // 4. 返回重定向地址和证书 - url & ticket
    if (Constant.SET_OK.equals(setResult)) {
        return ServerResponse.createBySuccess(ticket);
    } else {
        return ServerResponse.createByErrorMessage("用户名或者密码错误, 请重新尝试!");
    }
}
```

2. 用户凭证检验

```java
@PostMapping
public ServerResponse<String> certificate(@RequestBody String ticket) {
    if (StringUtils.isEmpty(ticket)) {
        return ServerResponse.createByError();
    }
    String result = ticket.replaceAll("\"", "");
    String userInfo = redisUtil.get(result);
    if (StringUtils.isNotEmpty(userInfo)) {
        // 反序列化
        UserDTO userDto = (UserDTO) SerializableUtil.serializeToObject(userInfo);
        log.info("UserInfo: [{}]", userDto);
        return ServerResponse.createBySuccess();
    } else {
        return ServerResponse.createByError();
    }
}
```

3. 用户请求拦截 - 通过自定义注解实现

```java
@Slf4j
@Aspect
@Component
public class PermissionAspect {

    @Around(value = "@annotation(com.zss.base.annotation.RequiredPermission)")
    public Object aroundPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (sra != null) {
            HttpServletRequest request = sra.getRequest();
            String ticket = request.getHeader(Constant.TICKET);
            String uri = "http://192.168.2.125:8880/certificate";
            CloseableHttpResponse closeableHttpResponse = HttpUtil.doPost(uri, ticket);
            if (closeableHttpResponse != null) {
                System.out.println("HttpStatus: " + closeableHttpResponse.getStatusLine());
                try {
                    HttpEntity entity = closeableHttpResponse.getEntity();
                    JSONObject jsonObject = JSONObject.parseObject(EntityUtils.toString(entity));
                    String status = jsonObject.getString("status");
                    if (ResponseCode.SUCCESS.getCode() == Integer.parseInt(status)) {
                        // 通过
                        return joinPoint.proceed();
                    } else {
                        // 身份校验未通过
                        // 【注】本来想[response.sendRedirect()]重定向的，但是不知道怎么操作，
                        // 现在只能先返回特定信息，交给前端来判断了
                        return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                                ResponseCode.ILLEGAL_ARGUMENT.getDesc());
                    }
                } catch (IOException e) {
                    log.error("出现未知异常: [{}]", e.getMessage());
                    return ServerResponse.createByErrorMessage("发生未知异常");
                }
            }
        }
        log.error("权限校验拦截失败");
        return ServerResponse.createByErrorMessage("请刷新重新尝试");
    }
}
```

4. 前端登录方法

```javascript
function login(data) {
    $.ajax({
        url: "http://192.168.2.125:8880/certificate/login",
        contentType: "application/json;charset=utf-8",
        type: "post",
        dataType: "json",
        data: JSON.stringify(data),
        success: function (result) {
            analysisResult(result);
        }
    });
}

/* 解析数据 */
function analysisResult(result) {
    if (result.status === 0) {
        document.cookie = "sso_ticket=" + result.data + ";pass=/";
        document.cookit = "domain_test=domain_test;path=/;domain=.baidu.com";
        alert("登录成功");
    } else {
        $("#password").val("").focus();
    }
}
```



5. 前端其他的请求

```javascript
function appTwo(data) {
    $.ajax({
        url: "http://192.168.2.125:8882/two",
        contentType: "application/json;charset=utf-8",
        type: "post",
        dataType: "json",
        beforeSend: function (XMLHttpRequest) {
            XMLHttpRequest.setRequestHeader("ticket", getCookie("sso_ticket"));
        },
        data: JSON.stringify(data),
        success: function (result) {
            analysisResult(result);
        }
    });
}

/**
 * 解析数据
 * @param result
 */
function analysisResult(result) {
    if (result.status === 2) {
        window.location.href = "/login.html";
    } else {
        alert(result);
    }
}
/**
 * 获取Cookie
 * @param cname cookie的name
 * @return {string} cookie的value
 */
function getCookie(cname) {
    let name = cname + "=";
    let ca = document.cookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i].trim();
        if (c.indexOf(name) === 0) return c.substring(name.length, c.length);
    }
    return "";
}
```

