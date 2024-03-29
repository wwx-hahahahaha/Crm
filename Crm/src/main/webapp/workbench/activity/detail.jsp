<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<base href="<%=basePath%>">
<html>
<head>
    <meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

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
        });

    </script>

    <script>

        $(function () {
            activityShow()

            //编辑按钮绑定数据到表单中
            $("#update-btn").click(function () {
                let id = "${param.id}"
                $.ajax({
                    url: "http://localhost:8080/Crm/workbench/activity/selectActivityByid.do",
                    type: "POST",
                    data: {
                        "id": id
                    },
                    dataType: "JSON",
                    success: function (data) {
                        let option = "";
                        $.each(data.user, function (i, e) {
                            option += "<option value='" + e.id + "'>" + e.name + "</option>";
                        })
                        $("#edit-Owner").html(option)

                        $("#edit-hiddenId").val(data.a.id)
                        $("#edit-Name").val(data.a.name)
                        $("#edit-startTime").val(data.a.startDate)
                        $("#edit-endTime").val(data.a.endDate)
                        $("#edit-cost").val(data.a.cost)
                        $("#edit-describe").val(data.a.description)
                    }
                })
            })

            remarkShow()
            $("#remarkBody").on("mouseover", ".remarkDiv", function () {
                $(this).children("div").children("div").show();
            })
            $("#remarkBody").on("mouseout", ".remarkDiv", function () {
                $(this).children("div").children("div").hide();
            })

            //添加评论并追加新建的评论
            $("#saveBtn").click(function () {
                let re = $("#activity-id").val()
                $.ajax({
                    url: "http://localhost:8080/Crm/workbench/activity/saveRemark.do",
                    type: "POST",
                    data: {
                        "id": $.trim($("#activity-id").val()),
                        "remark": $.trim($("#remark").val())
                    },
                    dataType: "JSON",
                    success: function (data) {
                        let html = '';
                        if (data.bo) {

                            html += '<div class="remarkDiv" style="height: 60px;" id="' + data.list.id + '">'
                            html += '<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">'
                            html += '	<div style="position: relative; top: -40px; left: 40px;" >'
                            html += '	<h5 id="remark-noteContent">' + data.list.noteContent + '</h5>'
                            if (data.list.editFlag == '0') {
                                html += '<font color="gray">市场活动</font> <font color="gray">-</font> <b>' + $("#name").html() + '</b> <small style="color: gray;" id="time-By">' + data.list.createTime + '由' + data.list.createBy + '</small>'
                            } else {
                                html += '<font color="gray">市场活动</font> <font color="gray">-</font> <b>' + $("#name").html() + '</b> <small style="color: gray;" id="time-By">' + data.list.editTime + '由' + data.list.editBy + '</small>'
                            }
                            html += '<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">'
                            html += '<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: red;"></span></a>'
                            html += '&nbsp;&nbsp;&nbsp;&nbsp;'

                            html += '<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" onclick="deleteById(\'' + data.list.id + '\')" style="font-size: 20px; color: red;"></span></a>'
                            html += '</div>'
                            html += '</div>'
                            html += '</div>'
                            $("#remarkDiv").before(html)
                            $("#remark").val('')
                        }
                    }
                })
            })

            //更新备注
            $("#updateRemarkBtn").click(function (){
                let remark= $("#noteContent").val();
                let id=$("#remarkId").val()
                $.ajax({
                    url:"http://localhost:8080/Crm/workbench/activity/updateRemark.do",
                    type:"POST",
                    dataType:"JSON",
                    data:{
                        "id":id,
                        "remark":remark
                    },
                    success:function (data){
                        if (data.bo){
                            $("#e"+id).html(data.list.editTime+"由"+data.list.editBy)
                            $("#w"+id).html(remark)
                            $("#editRemarkModal").modal("hide")
                        }else {
                            alert("修改备注失败")
                        }
                    }
                })
            })
        })


        //展示数据
        function activityShow() {
            let id = "${param.id}"
            $.ajax({
                url: "http://localhost:8080/Crm/workbench/activity/selectActivity.do",
                data: {
                    "id": id
                },
                type: "GET",
                dataType: "JSON",
                success: function (data) {
                    $("#activity-id").val(data.id)
                    $("#owner").html(data.owner)
                    $("#name").html(data.name)
                    $("#startTime").html(data.startDate)
                    $("#endTime").html(data.endDate)
                    $("#cost").html(data.cost)
                    $("#createBy").html(data.createBy)
                    $("#createTime").html(data.createTime)
                    if (data.editBy != null && data.editTime != null) {
                        $("#editBy").html(data.editBy)
                        $("#editTime").html(data.editTime)
                    } else {
                        $("#editBy").html("还未修过过此条信息")
                        $("#editTime").html()
                    }
                    $("#description").html(data.description)
                    $("#Campaigns").html("市场活动:" + data.name)
                }
            })
        }

        //展示评论
        function remarkShow() {
            let id = "${param.id}"
            $.ajax({
                url: "http://localhost:8080/Crm/workbench/activity/selectRemark.do",
                type: "GET",
                data: {
                    "id": id
                },
                dataType: "JSON",
                success: function (data) {
                    let html = '';
                    $.each(data, function (i, e) {
                        html += '<div class="remarkDiv" style="height: 60px;" id="' + e.id + '">'
                        html += '<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">'
                        html += '	<div style="position: relative; top: -40px; left: 40px;" >'
                        html += '	<h5 id="w'+e.id+'">' + e.noteContent + '</h5>'
                        if (e.editFlag == '0') {
                            html += '<font color="gray">市场活动</font> <font color="gray">-</font> <b>' + $("#name").html() + '</b> <small style="color: gray;" id="e'+e.id+'">' + e.createTime + '由' + e.createBy + '</small>'
                        } else {
                            html += '<font color="gray">市场活动</font> <font color="gray">-</font> <b>' + $("#name").html() + '</b> <small style="color: gray;" id="e'+e.id+'">' + e.editTime + '由' + e.editBy + '</small>'
                        }
                        html += '		<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">'
                        html += '		<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" onclick="updateRemark(\''+e.id+'\')" style="font-size: 20px; color: red;"></span></a>'
                        html += '&nbsp;&nbsp;&nbsp;&nbsp;'
                        html += '<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" onclick="deleteById(\'' + e.id + '\')" style="font-size: 20px; color: red;"></span></a>'
                        html += '</div>'
                        html += '</div>'
                        html += '</div>'
                    })
                    $("#remarkDiv").before(html)
                }
            })
        }

        //删除评论
        function deleteById(id) {
            if (confirm("是否删除")) {
                $.ajax({
                    url: "http://localhost:8080/Crm/workbench/activity/deleteRemark.do",
                    type: "POST",
                    data: {
                        "id": id
                    },
                    dataType: "JSON",
                    success: function (data) {
                        if (data.bo) {
                            alert("删除成功");
                            $("#" + id).remove();
                        }
                    }
                })
            }
        }

        //点击编辑图片打开修改的模态框
        function updateRemark(id){
            // alert(id)
            $("#editRemarkModal").modal("show")
            let count=$("#w"+id).html()
            $("#noteContent").val(count)
            $("#remarkId").val(id)
        }

    </script>

</head>
<body>

<!-- 修改市场活动备注的模态窗口 -->
<div class="modal fade" id="editRemarkModal" role="dialog">
    <%-- 备注的id --%>
    <input type="hidden" id="remarkId">
    <div class="modal-dialog" role="document" style="width: 40%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabels">修改备注</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="edit-describe" class="col-sm-2 control-label">内容</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="noteContent"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
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
                <h4 class="modal-title" id="myModalLabel">修改市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form">
                    <input type="hidden" id="edit-hiddenId">
                    <div class="form-group">
                        <label for="edit-Owner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-Owner">
                                <%--                                    <option>zhangsan</option>--%>
                                <%--                                    <option>lisi</option>--%>
                                <%--                                    <option>wangwu</option>--%>
                            </select>
                        </div>
                        <label for="edit-Name" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-Name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-startTime">
                        </div>
                        <label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-endTime">
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
                <button type="button" class="btn btn-primary" data-dismiss="modal">更新</button>
            </div>
        </div>
    </div>
</div>

<!-- 返回按钮 -->
<div style="position: relative; top: 35px; left: 10px;">
    <a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left"
                                                                         style="font-size: 20px; color: #DDDDDD"></span></a>
</div>

<!-- 大标题 -->
<div style="position: relative; left: 40px; top: -30px;">
    <div class="page-header">
        <h3 id="Campaigns">市场活动-发传单 <small>2020-10-10 ~ 2020-10-20</small></h3>
    </div>
    <div style="position: relative; height: 50px; width: 250px;  top: -72px; left: 700px;">
        <button type="button" class="btn btn-default" data-toggle="modal" data-target="#editActivityModal"
                id="update-btn"><span class="glyphicon glyphicon-edit"></span> 编辑
        </button>
        <button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
    </div>
</div>

<!-- 详细信息 -->
<div style="position: relative; top: -70px;">
    <div style="position: relative; left: 40px; height: 30px;">
        <input type="hidden" id="activity-id">
        <div style="width: 300px; color: gray;">所有者</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b id="owner"></b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b id="name"></b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>

    <div style="position: relative; left: 40px; height: 30px; top: 10px;">
        <div style="width: 300px; color: gray;">开始日期</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b id="startTime"></b></div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">结束日期</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b id="endTime"></b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 20px;">
        <div style="width: 300px; color: gray;">成本</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b id="cost">4,000</b></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 30px;">
        <div style="width: 300px; color: gray;">创建者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b id="createBy"></b><small
                style="font-size: 10px; color: gray;" id="createTime"></small></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 40px;">
        <div style="width: 300px; color: gray;">修改者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b id="editBy"></b><small
                style="font-size: 10px; color: gray;" id="editTime"></small></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 50px;">
        <div style="width: 300px; color: gray;">描述</div>
        <div style="width: 630px;position: relative; left: 200px; top: -20px;">
            <b id="description">
            </b>
        </div>
        <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
</div>

<!-- 备注 -->
<div style="position: relative; top: 30px; left: 40px;" id="remarkBody">
    <div class="page-header">
        <h4>备注</h4>
    </div>

    <!-- 备注1 -->
    <%--        <div class="remarkDiv" style="height: 60px;" id="beizhu">--%>
    <%--            <img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">--%>
    <%--            <div style="position: relative; top: -40px; left: 40px;">--%>
    <%--                <h5 id="remark-noteContent">哎呦！</h5>--%>
    <%--                <font color="gray">市场活动</font> <font color="gray">-</font> <b id="activity-name"></b> <small--%>
    <%--                    style="color: gray;" id="time-By"> 2017-01-22 10:10:10 由zhangsan</small>--%>
    <%--                <div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">--%>
    <%--                    <a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit"--%>
    <%--                                                                       style="font-size: 20px; color: #E6E6E6;"></span></a>--%>
    <%--                    &nbsp;&nbsp;&nbsp;&nbsp;--%>
    <%--                    <a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove"--%>
    <%--                                                                       style="font-size: 20px; color: #E6E6E6;"></span></a>--%>
    <%--                </div>--%>
    <%--            </div>--%>
    <%--        </div>--%>

    <div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
        <form role="form" style="position: relative;top: 10px; left: 10px;">
            <textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"
                      placeholder="添加备注..."></textarea>
            <p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
                <button id="cancelBtn" type="button" class="btn btn-default">取消</button>
                <button type="button" id="saveBtn" class="btn btn-primary">保存</button>
            </p>
        </form>
    </div>
    <div style="height: 200px;"></div>
</body>
</html>