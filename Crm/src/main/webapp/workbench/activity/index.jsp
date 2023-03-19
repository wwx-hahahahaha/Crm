<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<%@page isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css"
          rel="stylesheet"/>

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>


    <script type="text/javascript">

        $(function () {

            //表单时间插件
            $(".time").datetimepicker({
                minView: "month",
                language: 'zh-CN',
                format: 'yyyy-mm-dd',
                autoclose: true,
                todayBtn: true,
                pickerPosition: "bottom-left"
            });

            //点击创建将姓名添加到下拉框中
            $("#add").click(function () {

                let hg = '';
                $.ajax({
                    url: "http://localhost:8080/Crm/workbench/activity/getUserList.do",
                    type: "GET",
                    dataType: "JSON",
                    success: function (data) {
                        $.each(data, function (i, n) {//遍历传递过来的JSON数组
                            hg += "<option value='" + n.id + "'>" + n.name + "</option>"//根据数据长度拼接下拉框并将用户姓名绑定进去
                        })
                        $("#create-marketActivityOwner").html(hg)//将拼接好的字符串写进父类里面

                        let id = "${user.id}"
                        $("#create-marketActivityOwner").val(id);//根据下拉框的value进行父类的数据绑定
                        $("#createActivityModal").modal("show");//展示模态窗口

                    }
                })
            })
            //点击保存按钮触发事件,保存数据
            $("#Savebtn").click(function () {
                $.ajax({
                    url: "http://localhost:8080/Crm/workbench/activity/saveActivity.do",
                    type: "POST",
                    data: {//将表单中的数据以JSON对象的形式传递到后台
                        "owner": $.trim($("#create-marketActivityOwner").val()),
                        "name": $.trim($("#create-marketActivityName").val()),
                        "startDate": $.trim($("#create-startTime").val()),
                        "endDate": $.trim($("#create-endTime").val()),
                        "cost": $.trim($("#create-cost").val()),
                        "description": $.trim($("#create-describe").val())
                    },
                    dataType: "JSON",
                    success: function (data) {
                        if (data.bo) {
                            alert("添加成功'")
                            pageList(1, 2)
                            //清空表单
                            // $("#activityAddForm").submit();


                            $("#activityAddForm")[0].reset()
                            //关闭添加操作的模态窗口
                            $("#createActivityModal").modal("hide")

                        }
                    }
                })
                pageList(1, 2)
            })

            //刷新显示数据列表
            pageList(1, 2);

            //点击查询按钮，将表单中的数据添加到隐藏表单里
            $("#search-selectBtn").click(function () {
                $("#hidden-name").val($("#search-name").val())
                $("#hidden-owner").val($("#search-owner").val())
                $("#hidden-startDate").val($("#startTime").val())
                $("#hidden-endDate").val($("#endTime").val())
                pageList(1, 2)
            })

        });


        //定义一个方法,用于刷新表单数据显示那一块的内容
        function pageList(pageNo, pageSize) {
            //将查询框的数据传到隐藏框
            $("#search-name").val($("#hidden-name").val())
            $("#search-owner").val($("#hidden-owner").val())
            $("#startTime").val($("#hidden-startDate").val())
            $("#endTime").val($("#hidden-endDate").val())

            let html = "";
            $.ajax({
                url: "http://localhost:8080/Crm/workbench/activity/getActivity.do",
                type: "GET",
                dataType: "JSON",
                data: {
                    "pageNo": pageNo,
                    "pageSize": pageSize,
                    "name": $.trim($("#search-name").val()),
                    "owner": $.trim($("#search-owner").val()),
                    "startTime": $.trim($("#startTime").val()),
                    "endDate": $.trim($("#endTime").val())
                },
                success: function (data) {
                    //遍历传过来的对象,将数据拼接进行展示
                    $.each(data.list, function (i, e) {
                        html += "<tr class='active'>" +
                            "<td><input type='checkbox' value='" + e.id + "' name='xz'/></td>" +
                            "<td><a style='text-decoration: none; cursor: pointer;' class='abtn'>" + e.name + "</a></td>" +
                            "<td>" + e.owner + "</td>" +
                            "<td>" + e.startDate + "</td>" +
                            "<td>" + e.endDate + "</td>" +
                            "</tr>";
                    })
                    //拼接分页条
                    let li = "<li class='disabled'><a id='Home'>首页</a></li>" +
                        "<li class='disabled'><a >上一页</a></li>";
                    let nuns = data.total % 2 == 0 ? (data.total / 2) : Math.ceil(data.total / 2);
                    for (let i = 1; i < nuns; i++) {
                        if (i == 1) {
                            li += "<li><a class='ye'>1</a></li>";
                        }
                        li += "<li><a class='ye'>" + (i + 1) + "</a></li>";
                    }
                    li += "<li><a>下一页</a></li>" +
                        "<li class='disabled'><a>末页</a></li>"
                    $("#ye-ul").html(li)

                    //翻页
                    $(".ye").each(function () {
                        $(this).on("click", function () {
                            pageList(this.innerHTML, 2);
                            // $(this).parent("li").addClass('.active')
                        })
                    })

                    //全选选中绑定所有选择框
                    $("#qx").click(function () {
                        $("input[name=xz]").prop("checked", this.checked)
                    })
                    //如果所有选择框选中则全选被选中
                    $("#bbody").on("click", $("input[name=xz]"), function () {
                        $("#qx").prop("checked", $("input[name=xz]").length == $("input[name=xz]:checked").length)
                    })

                    //添加查询出来的总条数
                    $("#total").text(data.total)
                    //将拼接好的数据显示添加到tablebody里面
                    $("#bbody").html(html);

                    //删除按钮绑定删除事件
                    $("#delete").click(function () {
                        let id = '';
                        let qz = $("input[name=xz]:checked")
                        if (qz.length == 0) {
                            alert("请先选择数据")
                        } else {
                            //将id拼接起来,便于发送到后端进行删除操作
                            for (let i = 0; i < qz.length; i++) {
                                id += "id=" + $(qz[i]).val()
                                if (i < qz.length - 1) {
                                    id += "&"
                                }
                            }
                            if (confirm("是否删除")) {
                                $.ajax({
                                    url: "http://localhost:8080/Crm/workbench/activity/delete.do",
                                    type: "POST",
                                    data: id,//这里因为key会重复,所以不能使用JSON对象发送,直接发送传统请求的字符串
                                    dataType: "JSON",
                                    success: function (data) {
                                        if (data) {
                                            pageList(1, 2)
                                        } else {
                                            alert("删除失败")
                                        }
                                    }
                                })
                            }
                        }
                    })


                    $(".abtn").click(function () {
                        let id=$(this).parent().prev().children("input").val()
                        window.location.href = "workbench/activity/detail.jsp?id="+id;
                    })

                    //点击修改按钮绑定数据
                    $("#update-btn").click(function () {
                        let $xz=$("input[name=xz]:checked");
                        if ($xz.length==0){
                            alert("当前未选择")
                        }else if ($xz.length>1){
                            alert("只能选择一个以进行修改")
                        }else{
                            let id=$xz.val();
                            $.ajax({
                                url: "http://localhost:8080/Crm/workbench/activity/selectActivityByid.do",
                                type: "POST",
                                data: {
                                    "id": id
                                },
                                dataType: "JSON",
                                success: function (data) {
                                    let option="";
                                    $.each(data.user,function (i,e){
                                        option+="<option value='"+e.id+"'>"+e.name+"</option>";
                                    })
                                    $("#edit-Owner").html(option)

                                    $("#edit-hiddenId").val(data.a.id)
                                    $("#edit-marketActivityName").val(data.a.name)
                                    $("#edit-startTime").val(data.a.startDate)
                                    $("#edit-endTime").val(data.a.endDate)
                                    $("#edit-cost").val(data.a.cost)
                                    $("#edit-describe").val(data.a.description)
                                }
                            })
                        }
                    })


                    //点击更新修改数据
                    $("#update").click(function () {
                        if (confirm("是否修改")) {
                            $.ajax({
                                url: "http://localhost:8080/Crm/workbench/activity/update.do",
                                type: "POST",
                                data: {
                                    "id": $.trim($("#edit-hiddenId").val()),
                                    "owner":$.trim($("#edit-Owner").val()),
                                    "name": $.trim($("#edit-marketActivityName").val()),
                                    "startTime": $.trim($("#edit-startTime").val()),
                                    "endTime": $.trim($("#edit-endTime").val()),
                                    "cost": $.trim($("#edit-cost").val()),
                                    "describe": $.trim($("#edit-describe").val())
                                },
                                dataType: "JSON",
                                success: function (data) {
                                    if (data.bo) {
                                        alert("修改成功")
                                        pageList(1, 2)
                                    }
                                }
                            })
                            pageList(1, 2)
                        }

                    })
                }
            })
        };

        //根据id查询activity和user将数据填入修改框中的方法

    </script>
</head>
<body>

<input type="hidden" id="hidden-name">
<input type="hidden" id="hidden-owner">
<input type="hidden" id="hidden-startDate">
<input type="hidden" id="hidden-endDate">

<!-- 创建市场活动的模态窗口 -->
<div class="modal fade" id="createActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form" id="activityAddForm">

                    <div class="form-group">
                        <label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-marketActivityOwner">

                            </select>
                        </div>
                        <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-marketActivityName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-startTime" readonly>
                        </div>
                        <label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-endTime" readonly>
                        </div>
                    </div>
                    <div class="form-group">

                        <label for="create-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-cost">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-describe"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal" id="Savebtn">保存</button>
            </div>
        </div>
    </div>
</div>

<!-- 修改市场活动的模态窗口 -->
<div class="modal fade" id="editActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form">
                    <input type="hidden" id="edit-hiddenId">
                    <div class="form-group">
                        <label for="edit-Owner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-Owner">
                                <%--                                <option>zhangsan</option>--%>
                                <%--                                <option>lisi</option>--%>
                                <%--                                <option>wangwu</option>--%>
                            </select>
                        </div>
                        <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-marketActivityName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="edit-startTime" readonly>
                        </div>
                        <label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="edit-endTime" readonly>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-cost">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-describe"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal" id="update">更新</button>
            </div>
        </div>
    </div>
</div>


<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>市场活动列表</h3>
        </div>
    </div>
</div>
<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">名称</div>
                        <input class="form-control" type="text" id="search-name">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="search-owner">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">开始日期</div>
                        <input class="form-control" type="text" id="startTime"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">结束日期</div>
                        <input class="form-control" type="text" id="endTime">
                    </div>
                </div>

                <button type="button" class="btn btn-default" id="search-selectBtn">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary" id="add"><span class="glyphicon glyphicon-plus"></span> 创建
                </button>
                <button type="button" class="btn btn-default" data-toggle="modal" data-target="#editActivityModal"
                        id="update-btn"><span
                        class="glyphicon glyphicon-pencil"></span> 修改
                </button>
                <button type="button" class="btn btn-danger" id="delete"><span class="glyphicon glyphicon-minus"></span>
                    删除
                </button>
            </div>

        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="qx"/></td>
                    <td>名称</td>
                    <td>所有者</td>
                    <td>开始日期</td>
                    <td>结束日期</td>
                </tr>
                </thead>
                <tbody id="bbody">
                <%--						<tr class="active">--%>
                <%--							<td><input type="checkbox" /></td>--%>
                <%--							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>--%>
                <%--                            <td>zhangsan</td>--%>
                <%--							<td>2020-10-10</td>--%>
                <%--							<td>2020-10-20</td>--%>
                <%--						</tr>--%>
                </tbody>
            </table>
        </div>

        <div style="height: 50px; position: relative;top: 30px;">
            <div>
                <button type="button" class="btn btn-default" style="cursor: default;">共<b id="total"></b>条记录
                </button>
            </div>
            <div class="btn-group" style="position: relative;top: -34px; left: 110px;">
                <button type="button" class="btn btn-default" style="cursor: default;">显示</button>
                <div class="btn-group">
                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                        2
                    </button>
                </div>
                <button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
            </div>
            <div style="position: relative;top: -88px; left: 285px;">
                <nav>
                    <ul class="pagination" id="ye-ul">
                        <%--                        <li class="disabled"><a id="Home">首页</a></li>--%>
                        <%--                        <li class="disabled"><a >上一页</a></li>--%>
                        <%--                        <li class="active"><a href="#">1</a></li>--%>
                        <%--                        <li><a >2</a></li>--%>
                        <%--                        <li><a>3</a></li>--%>
                        <%--                        <li><a>4</a></li>--%>
                        <%--                        <li><a>5</a></li>--%>
                        <%--                        <li><a>下一页</a></li>--%>
                        <%--                        <li class="disabled"><a>末页</a></li>--%>
                    </ul>
                </nav>
            </div>
        </div>

    </div>
</div>
</body>
</html>