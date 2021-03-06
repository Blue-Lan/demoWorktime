<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>工時系統</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.2/css/bootstrap.min.css" integrity="sha384-Smlep5jCw/wG7hdkwQ/Z5nLIefveQRIY9nfy6xoR1uRYBtpZgI6339F5dgvm/e9B"
        crossorigin="anonymous">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.1.0/css/all.css" integrity="sha384-lKuwvrZot6UHsBSfcMvOkWwlCMgc0TaWr+30HWe3a4ltaBwTZhyTEggF5tJv8tbt"
        crossorigin="anonymous">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.min.js"></script>
    <style>
        html {
            font-size: 20px;
        }

        .checkboxSize {
            width: 30px;
            height: 30px;
        }
    </style>
</head>

<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark py-3 font-weight-bold">
        <a class="navbar-brand" href="mgrMain.html">工時系統</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarNavDropdown"
            aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNavDropdown">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                    <a class="nav-link" href="mgrMain.html">首頁
                        <span class="sr-only">(current)</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="mgrCheckWorktime.html">審核工時</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">催繳工時</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="mgrSearchWorktime.html">查詢員工工時</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="mgrSearchEmpInfo.html">查詢員工資料</a>
                </li>
            </ul>
            <div class="btn-group mr-2">
                <button type="button" class="btn btn-success">
                    <i class="fas fa-user mr-2"></i>林彥儒</button>
                <button type="button" class="btn btn-success dropdown-toggle dropdown-toggle-split" data-toggle="dropdown" aria-haspopup="true"
                    aria-expanded="false">
                    <span class="sr-only">Toggle Dropdown</span>
                </button>
                <div class="dropdown-menu">
                    <button type="button" class="dropdown-item" data-toggle="modal" data-target="#personInfo">
                        <i class="fas fa-info mr-3"></i>個人資料
                    </button>
                    <div class="dropdown-divider"></div>
                    <button type="button" class="dropdown-item" data-toggle="modal" data-target="#changePwd" data-backdrop="static">
                        <i class="fas fa-unlock-alt mr-2"></i>變更密碼
                    </button>
                </div>
            </div>
            <a href="login.html" class="btn btn-warning navBtn font-weight-bold mr-3">
                <i class="fas fa-sign-out-alt mr-1"></i>登出</a>
        </div>
    </nav>
    <div class="container mt-4">
        <div class="row justify-content-center h5 alert alert-danger font-weight-bold">
            一天只限催繳一次
        </div>
        <table class="table text-center table-hover ">
            <thead class="thead-dark">
                <tr>
                    <th style="width: 25%" class="align-middle">日期</th>
                    <th class="align-middle">員工編號</th>
                    <th class="align-middle">員工姓名</th>
                    <th style="width: 25%;">催繳次數</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="noSubmotWorktimeList" items="${requestScope.noSubmotWorktimeList}" varStatus="loop">
                	<tr>
                		<td><c:out value="${noSubmotWorktimeList.weekFirstdate}"/></td>
                		<td><c:out value="${noSubmotWorktimeList.empno}"/></td>
                		<td><c:out value="${noSubmotWorktimeList.name}"/></td>
                		<td><c:out value="${noSubmotWorktimeList.urgeTimes}"/></td>
                	</tr>
                </c:forEach>
                <tr>
                    <td colspan="4">
                        <a href="Worktime?action=urgeWorktime" class="btn btn-warning callWorktime font-weight-bold">催繳</a>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <footer class="bg-secondary text-white text-center fixed-bottom">
        工時系統 Copyright © 2018 YanRu Lin All rights reserved
    </footer>
    <!-- personal Modal-->
    <div class="modal fade" id="personInfo" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header bg-info text-light">
                    <h5 class="modal-title" id="exampleModalLabel">
                        <i class="fas fa-info mr-3"></i>個人資料</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body px-0 py-0">
                    <table class="table text-center">
                        <tr>
                            <td>員工編號</td>
                            <td> 00000000</td>
                        </tr>
                        <tr>
                            <td>姓名</td>
                            <td> 林彥儒</td>
                        </tr>
                        <tr>
                            <td>email</td>
                            <td>yanru4021470518@gamil.com</td>
                        </tr>
                        <tr>
                            <td>職位</td>
                            <td>員工</td>
                        </tr>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary btn-block" data-dismiss="modal">關閉</button>
                </div>
            </div>
        </div>
    </div>

    <!-- changePwd Modal -->
    <div class="modal fade" id="changePwd" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header bg-warning">
                    <h5 class="modal-title text-center" id="exampleModalLabel">
                        <i class="fas fa-unlock-alt mr-2"></i>變更密碼</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form class="form" role="form">
                        <div class="form-group">
                            <label>請輸入舊密碼</label>
                            <input name="old_pw" id="old_pw" class="form-control old-pw" type="password">
                            <span class="error-msg" style="color: red"></span>
                        </div>
                        <div class="form-group">
                            <label>請輸入新密碼</label>
                            <input id="user-pwd-input" name="user-pwd-input" type="password" placeholder="請輸入不得為空且少於8位的數字與英文組合" class="new-pw form-control"
                            />
                            <span class="error-msg" style="color: red"></span>
                        </div>
                        <div class="form-group">
                            <label>請再次輸入新密碼</label>
                            <input placeholder="再次新確認密碼" class="form-control new-pw" type="password" name="user_pwd_again" />
                            <span class="error-msg" style="color: red"></span>
                        </div>

                        <div class="modal-footer pr-0">
                            <button type="button" class="btn btn-success">確定變更</button>
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>

                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.4.0/Chart.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49"
        crossorigin="anonymous"></script>   
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.2/js/bootstrap.min.js" integrity="sha384-o+RDsa0aLu++PJvFqy8fFScvbHFLtbvScb8AjopnFD+iEQ7wo/CG0xlczd+2O/em"
        crossorigin="anonymous"></script>
        <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
</body>

</html>