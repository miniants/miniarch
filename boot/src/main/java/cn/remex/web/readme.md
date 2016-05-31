##说明
cn.remex.web组件是为实现web快速开发进行的开源组合轻量级开发框架.

##版本说明
V0.1-miniarch
====================================
本版本在Remex2团队之前的基础上进行的轻量重构,为达到微服务的开发级别,同时具备良好的开源框架可替代性,业务可拓展性.


##Web服务流程
以下用词注意:客户端-指调用本平台服务的对方;服务端-本平台在调用对方服务时的自指.
1.客户端调用过程
-------------------
①客户请求
-> ②[web开源组件:request]   //目前平台使用Spring MVC,允许基于JVM的web框架定制. 负责提供Uri路由及原始数据（BsCvo&BsRvo初始化,HttpRequest,HttpRespone)的接收.
-> ③[数据结构适配:inbound]   //根据每个领域的服务,基于URI规则的数据结构适配.1)实现DataStructureAdapter 2)uri与DataStructureAdapter的映射关系
-> ④本开发框架通用服务调用   //ServiceFactory.invoke提供通用的服务调用机制,传送BsCvo,并初始BsRvo
-> ⑤基于业务逻辑的服务       //根据业务逻辑实现Bs,请求在BsCvo中,响应在BsRvo中.
-> ④本开发框架通用服务调用    //BsFactory提供通用的服务响应机制,接收BsCvo,并处理BsRvo
-> ③[数据结构适配:outbound]
-> ②[web开源组件:response]
-> ①服务响应

2.服务端请求过程
-------------------
①服务请求业务逻辑 //按业务逻辑拼装BsCvo
-> ②本开发框架通用服务请求  //ServiceFactory.invoke提供通用的请求调用机制,传送BsCvo,并初始BsRvo
-> ③[数据结构适配:outbound]
-> ④[web开源组件服务调用]
-> ⑤被调用端处理
-> ④[web开源组件服务调用]
-> ③[数据结构适配:inbound]
-> ②本开发框架通用服务请求
-> ①服务请求业务逻辑


##Web调用接口设计
1 cn.remex.web.service.ServiceFactory
-------------------
    .invoke(BsCvo bsCvo,BsRvo bsRvo) //客户请求时,平台通用的调用机制
    .invoke(String rssr,BsCvo bsvo) //服务请求时,平台通用的请求机制
1.1 cn.remex.web.service.BsCvo
    String bs;                  //必选,调用的Bs名称或映射值
    String bsCmd = "execute";   //可选,调用的Bs的方法名
    String rt;					//可选,返回的类型，
    String rv;					//可选,返回结构的值，
    String pk;                  //可选, LHY 2015-6-21 RESTFul模式设计中的resource/pk主键 部分。 http://host:prot/app/services/resources services:bs-bsCmdresources:beans/num/subBeans/num 循环beans/num
    String sid;                 //可选, 会话id
    String flowNo;              //可选, 流程流水号
    String transNo;             //可选, 交易流水号
    String cpt;                 //可选,调用端组件标识
    String mid;					//可选,随机码
    String ef;			        //可选,是否加密 encryprFlag
    String et;			        //可选,加密类型 encryptType
    String tkn;					//可选,本次交易的token值，避免重复提交。
    String stk;                 //可选,身份验证security token
    Object body;                //可选,业务逻辑所需的请求数据对象

1.2 cn.remex.web.service.BsRvo
    String status;              //必选
    String msg;                 //可选,响应的消息,需与code成对
    String code;                //可选,响应的消息代码,需与msg成对
    Object body;                //可选,业务逻辑所需的响应数据对象

1.3 String:rssr
    范式格式:rssr://[(host[:port])/app/domain/]bs/[bsCmd[/pk]|/pk][.json|.xml]
                   |<----主机及上下文部分----->||<--Restful------>||<--协议-—->|

2 cn.remex.web.da.DataStructureAdapter
    .inbound(BsCvo bsCvo, BsRvo bsRvo, HttpRequest httpRequest, HttpResponse httpResponse)      //
    .outbound(BsCvo bsCvo, BsRvo bsRvo)     //
