<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.rose.settings.domain.dicValue" %>
<%@ page import="java.util.List" %>
<%@ page import="com.rose.workbench.domain.Tran" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";

    //先取出状态的字典数据
    List<dicValue> list = (List<dicValue>) application.getAttribute("stage");
    //拿到状态对应的可能性的集合
    Map<String, String> map = (Map<String, String>) application.getAttribute("pMap");
    int count = 0;
    //拿到可能性100和0的分隔
    for (int i = 0; i < list.size(); i++) {
        String value = list.get(i).getValue();
        String s = map.get(value);
        if ("0".equals(s)) {
            count = i;
            break;
        }
    }

%>
<%@page isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">

    <link href="../../jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>

    <style type="text/css">
        .mystage {
            font-size: 20px;
            vertical-align: middle;
            cursor: pointer;
        }

        .closingDate {
            font-size: 15px;
            cursor: pointer;
            vertical-align: middle;
        }
    </style>

    <script type="text/javascript" src="../../jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="../../jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

    <script type="text/javascript">

        //默认情况下取消和保存按钮是隐藏的
        var cancelAndSaveBtnDefault = true;

        $(function () {
            $("#remark").focus(function () {
                if (cancelAndSaveBtnDefault) {
                    //设置remarkDiv的高度为130px
                    $("#remarkDiv").css("height", "130px");
                    //显示
                    $("#cancelAndSaveBtn").show("2000");
                    cancelAndSaveBtnDefault = false;
                }
            });

            $("#cancelBtn").click(function () {
                //显示
                $("#cancelAndSaveBtn").hide();
                //设置remarkDiv的高度为130px
                $("#remarkDiv").css("height", "90px");
                cancelAndSaveBtnDefault = true;
            });

            $(".remarkDiv").mouseover(function () {
                $(this).children("div").children("div").show();
            });

            $(".remarkDiv").mouseout(function () {
                $(this).children("div").children("div").hide();
            });

            $(".myHref").mouseover(function () {
                $(this).children("span").css("color", "red");
            });

            $(".myHref").mouseout(function () {
                $(this).children("span").css("color", "#E6E6E6");
            });


            //阶段提示框
            $(".mystage").popover({
                trigger: 'manual',
                placement: 'bottom',
                html: 'true',
                animation: false
            }).on("mouseenter", function () {
                var _this = this;
                $(this).popover("show");
                $(this).siblings(".popover").on("mouseleave", function () {
                    $(_this).popover('hide');
                });
            }).on("mouseleave", function () {
                var _this = this;
                setTimeout(function () {
                    if (!$(".popover:hover").length) {
                        $(_this).popover("hide")
                    }
                }, 100);
            });

            history();

        });

        //点击图标修改交易历史和图标的方法
        function changeCtage(stage, i) {
            $.ajax({
                url: "workbench/transaction/UpdateTran.do",
                type: "POST",
                data: {
                    "tranId":"${s.id}",
                    "stage":stage,
                    "money":"${s.money}",
                    "data":"${s.expectedDate}",
                },
                dataType: "JSON",
                success: function (data) {
                    if (data.bo){
                        $("#possible").html(data.list.kn)
                        $("#stage").html(stage)
                        $("#editBy").html(data.list.createBy)
                        $("#editTime").html(data.list.createTime)
                        history();
                    }
                }
            })
            IconRefresh(i);

        }

        //交易历史数据展示
        function history() {

            $.ajax({
                url: "workbench/transaction/getTranHistory.do",
                type: "GET",
                dataType: "JSON",
                data: {
                    "id": "${s.id}"
                },
                success: function (data) {
                    let html = '';
                    $.each(data, function (i, e) {
                        html += '<tr>'
                        html += '	<td>' + e.stage + '</td>'
                        html += '	<td>' + e.money + '</td>'
                        html += '	<td>' + e.kn + '</td>'
                        html += '	<td>' + e.expectedDate + '</td>'
                        html += '	<td>' + e.createTime + '</td>'
                        html += '	<td>' + e.createBy + '</td>'
                        html += '</tr>'
                    })
                    $("#tbody").html(html)
                }
            })
        }

        //图标刷新
        function IconRefresh(counts){
            let count="<%=count%>"
            let stage=$("#stage").html();
            let possible=$("#possible").html();

            if (possible=='0'){//如果当前状态等于0，说明前面七个都是黑色圆圈
                for (let i = 0; i < count; i++) {//前面都是黑色圆圈
                    $("#"+i).removeClass();
                    $("#"+i).addClass("glyphicon glyphicon-record");
                    $("#"+i).css("color","#000000");
                }
                for (let i = count; i <"<%list.size();%>" ; i++) {
                    if (i==counts){//红叉
                        $("#"+i).removeClass();
                        $("#"+i).addClass("glyphicon glyphicon-remove");
                        $("#"+i).css("color","#Ff0000");
                    }else{//黑叉
                        $("#"+i).removeClass();
                        $("#"+i).addClass("glyphicon glyphicon-remove");
                        $("#"+i).css("color","#000000");
                    }
                }
            }else{
                for (let i = 0; i < count; i++) {
                    if (i==counts){//绿坐标
                        $("#"+i).removeClass();
                        $("#"+i).addClass("glyphicon glyphicon-map-marker");
                        $("#"+i).css("color","#90F790");
                    }else if (i<counts){//绿色对勾
                        $("#"+i).removeClass();
                        $("#"+i).addClass("glyphicon glyphicon-ok-circle");
                        $("#"+i).css("color","#90F790");
                    }else if (i>counts){//黑色圆圈
                        $("#"+i).removeClass();
                        $("#"+i).addClass("glyphicon glyphicon-record");
                        $("#"+i).css("color","#000000");
                    }
                }
                for (let i = count; i <"<%list.size();%>" ; i++) {//后面都是黑色叉
                    $("#"+i).removeClass();
                    $("#"+i).addClass("glyphicon glyphicon-remove");
                    $("#"+i).css("color","#000000");
                }
            }
        }


    </script>

</head>
<base href="<%=basePath%>">
<body>

<!-- 返回按钮 -->
<div style="position: relative; top: 35px; left: 10px;">
    <a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left"
                                                                         style="font-size: 20px; color: #DDDDDD"></span></a>
</div>

<!-- 大标题 -->
<div style="position: relative; left: 40px; top: -30px;">
    <div class="page-header">
        <h3>${s.customerId}<small>￥${s.money}</small></h3>
    </div>
    <div style="position: relative; height: 50px; width: 250px;  top: -72px; left: 700px;">
        <button type="button" class="btn btn-default" onclick="window.location.href='edit.html';"><span
                class="glyphicon glyphicon-edit"></span> 编辑
        </button>
        <button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
    </div>
</div>

<!-- 阶段状态 -->
<div style="position: relative; left: 40px; top: -50px;">
    <%
        Tran tran = (Tran) request.getAttribute("s");
        String stage = tran.getStage();
        String knx = map.get(stage);
        if ("0".equals(knx)) {//表示当前阶段是后面几个叉
            for (int i = 0; i < list.size(); i++) {//这里遍历，判断其它的阶段对应的信息
                String value = list.get(i).getValue();
                String possible = map.get(value);
                if ("0".equals(possible)) {//判断当前遍历出来阶段的可能性是否为0,如果为0则是叉
                    if (value.equals(stage)) {//判断当前阶段是否为循环的这个阶段，是的话应该是红叉
    %>
    <span id="<%=i%>" class="glyphicon glyphicon-remove mystage" onclick="changeCtage('<%=value%>','<%=i%>')"
          data-toggle="popover" data-placement="bottom"
          data-content="<%=value%>" style="color: red;"></span>-------------
    <%
    } else {//如果不是，则为黑叉
    %>
    <span id="<%=i%>" class="glyphicon glyphicon-remove mystage" onclick="changeCtage('<%=value%>','<%=i%>')"
          data-toggle="popover" data-placement="bottom"
          data-content="<%=value%>" style="color: #000000;"></span>-------------
    <%
        }
    } else {//如果不是0则是黑圈
    %>
    <span id="<%=i%>" class="glyphicon glyphicon-record mystage" onclick="changeCtage('<%=value%>','<%=i%>')"
          data-toggle="popover" data-placement="bottom"
          data-content="<%=value%>" style="color: #000000;"></span>-------------
    <%
            }
        }
    } else {//表示当前阶段是前面几个圈
        int index = 0;
        for (int i = 0; i < list.size(); i++) {
            String value = list.get(i).getValue();
            String possible = map.get(value);
            if (value.equals(stage)) {
                index = i;
                break;
            }
        }
        for (int i = 0; i < list.size(); i++) {
            String value = list.get(i).getValue();
            String possible = map.get(value);
            if ("0".equals(possible)) {//如果是0，则是黑叉
    %>
    <span id="<%=i%>" class="glyphicon glyphicon-remove mystage" onclick="changeCtage('<%=value%>','<%=i%>')"
          data-toggle="popover" data-placement="bottom"
          data-content="<%=value%>" style="color: #000000;"></span>-------------
    <%
    } else {//如果不是0,则是圈
        if (i == index) {//绿色坐标
    %>
    <span id="<%=i%>" class="glyphicon glyphicon-map-marker mystage" onclick="changeCtage('<%=value%>','<%=i%>')"
          data-toggle="popover" data-placement="bottom"
          data-content="<%=value%>" style="color: #90F790;"></span>-------------
    <%
    } else if (i < index) {//绿色对勾
    %>
    <span id="<%=i%>" class="glyphicon glyphicon-ok-circle mystage" onclick="changeCtage('<%=value%>','<%=i%>')"
          data-toggle="popover" data-placement="bottom"
          data-content="<%=value%>" style="color: #90F790;"></span>-------------
    <%
    } else if (i > index) {
        //黑色圈
    %>
    <span id="<%=i%>" class="glyphicon glyphicon-record mystage" onclick="changeCtage('<%=value%>','<%=i%>')"
          data-toggle="popover" data-placement="bottom"
          data-content="<%=value%>" style="color: #000000;"></span>-------------
    <%
                    }
                }
            }
        }
    %>


    <%--		阶段&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--%>
    <%--		<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="资质审查" style="color: #90F790;"></span>--%>
    <%--		-------------%>
    <%--		<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="需求分析" style="color: #90F790;"></span>--%>
    <%--		-------------%>
    <%--		<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="价值建议" style="color: #90F790;"></span>--%>
    <%--		-------------%>
    <%--		<span class="glyphicon glyphicon-ok-circle mystage" data-toggle="popover" data-placement="bottom" data-content="确定决策者" style="color: #90F790;"></span>--%>
    <%--		-------------%>
    <%--		<span class="glyphicon glyphicon-map-marker mystage" data-toggle="popover" data-placement="bottom" data-content="提案/报价" style="color: #90F790;"></span>--%>
    <%--		-------------%>
    <%--		<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="谈判/复审"></span>--%>
    <%--		-------------%>
    <%--		<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="成交"></span>--%>
    <%--		-------------%>
    <%--		<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="丢失的线索"></span>--%>
    <%--		-------------%>
    <%--		<span class="glyphicon glyphicon-record mystage" data-toggle="popover" data-placement="bottom" data-content="因竞争丢失关闭"></span>--%>
    <%--		-------------%>
    <span class="closingDate"><%=tran.getCreateTime()%></span>
</div>

<!-- 详细信息 -->
<div style="position: relative; top: 0px;">
    <div style="position: relative; left: 40px; height: 30px;">
        <div style="width: 300px; color: gray;">所有者</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${s.owner}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">金额</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${s.money}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 10px;">
        <div style="width: 300px; color: gray;">名称</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${s.name}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">预计成交日期</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${s.expectedDate}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 20px;">
        <div style="width: 300px; color: gray;">客户名称</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${s.customerId}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">阶段</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b id="stage">${s.stage}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    -
    <div style="position: relative; left: 40px; height: 30px; top: 30px;">
        <div style="width: 300px; color: gray;">类型</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${s.type}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">可能性</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b id="possible">${s.map}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 40px;">
        <div style="width: 300px; color: gray;">来源</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${s.source}</b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">市场活动源</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${s.activityId}</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 50px;">
        <div style="width: 300px; color: gray;">联系人名称</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${s.contactsId}</b></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 60px;">
        <div style="width: 300px; color: gray;">创建者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${s.createBy}</b><small
                style="font-size: 10px; color: gray;">${s.createTime}</small></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 70px;">
        <div style="width: 300px; color: gray;">修改者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b id="editBy">${s.editBy}</b><small
                style="font-size: 10px; color: gray;" id="editTime">${s.editTime}</small></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 80px;">
        <div style="width: 300px; color: gray;">描述</div>
        <div style="width: 630px;position: relative; left: 200px; top: -20px;">
            <b>
                ${s.description}
            </b>
        </div>
        <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 90px;">
        <div style="width: 300px; color: gray;">${s.contactSummary}</div>
        <div style="width: 630px;position: relative; left: 200px; top: -20px;">
            <b>
                &nbsp;
            </b>
        </div>
        <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 100px;">
        <div style="width: 300px; color: gray;">下次联系时间</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>&nbsp; ${s.nextContactTime}</b>
            <p></p></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
</div>

<!-- 备注 -->
<div style="position: relative; top: 100px; left: 40px;">
    <div class="page-header">
        <h4>备注</h4>
    </div>

    <!-- 备注1 -->
    <div class="remarkDiv" style="height: 60px;">
        <img title="zhangsan" src="../../image/user-thumbnail.png" style="width: 30px; height:30px;">
        <div style="position: relative; top: -40px; left: 40px;">
            <h5>哎呦！</h5>
            <font color="gray">交易</font> <font color="gray">-</font> <b>动力节点-交易01</b> <small
                style="color: gray;"> 2017-01-22 10:10:10 由zhangsan</small>
            <div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
                <a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit"
                                                                   style="font-size: 20px; color: #E6E6E6;"></span></a>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove"
                                                                   style="font-size: 20px; color: #E6E6E6;"></span></a>
            </div>
        </div>
    </div>

    <!-- 备注2 -->
    <div class="remarkDiv" style="height: 60px;">
        <img title="zhangsan" src="../../image/user-thumbnail.png" style="width: 30px; height:30px;">
        <div style="position: relative; top: -40px; left: 40px;">
            <h5>呵呵！</h5>
            <font color="gray">交易</font> <font color="gray">-</font> <b>动力节点-交易01</b> <small
                style="color: gray;"> 2017-01-22 10:20:10 由zhangsan</small>
            <div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
                <a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit"
                                                                   style="font-size: 20px; color: #E6E6E6;"></span></a>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove"
                                                                   style="font-size: 20px; color: #E6E6E6;"></span></a>
            </div>
        </div>
    </div>

    <div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
        <form role="form" style="position: relative;top: 10px; left: 10px;">
            <textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"
                      placeholder="添加备注..."></textarea>
            <p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
                <button id="cancelBtn" type="button" class="btn btn-default">取消</button>
                <button type="button" class="btn btn-primary">保存</button>
            </p>
        </form>
    </div>
</div>

<!-- 阶段历史 -->
<div>
    <div style="position: relative; top: 100px; left: 40px;">
        <div class="page-header">
            <h4>阶段历史</h4>
        </div>
        <div style="position: relative;top: 0px;">
            <table id="activityTable" class="table table-hover" style="width: 900px;">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td>阶段</td>
                    <td>金额</td>
                    <td>可能性</td>
                    <td>预计成交日期</td>
                    <td>创建时间</td>
                    <td>创建人</td>
                </tr>
                </thead>
                <tbody id="tbody">
                <%--						<tr>--%>
                <%--							<td>资质审查</td>--%>
                <%--							<td>5,000</td>--%>
                <%--							<td>10</td>--%>
                <%--							<td>2017-02-07</td>--%>
                <%--							<td>2016-10-10 10:10:10</td>--%>
                <%--							<td>zhangsan</td>--%>
                <%--						</tr>--%>
                <%--						<tr>--%>
                <%--							<td>需求分析</td>--%>
                <%--							<td>5,000</td>--%>
                <%--							<td>20</td>--%>
                <%--							<td>2017-02-07</td>--%>
                <%--							<td>2016-10-20 10:10:10</td>--%>
                <%--							<td>zhangsan</td>--%>
                <%--						</tr>--%>
                <%--						<tr>--%>
                <%--							<td>谈判/复审</td>--%>
                <%--							<td>5,000</td>--%>
                <%--							<td>90</td>--%>
                <%--							<td>2017-02-07</td>--%>
                <%--							<td>2017-02-09 10:10:10</td>--%>
                <%--							<td>zhangsan</td>--%>
                <%--						</tr>--%>
                </tbody>
            </table>
        </div>

    </div>
</div>

<div style="height: 200px;"></div>

</body>
</html>