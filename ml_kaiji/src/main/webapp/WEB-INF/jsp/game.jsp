<%@page import="java.io.Writer"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=1100, user-scalable=no">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="${context_path}/resources/js/jquery-3.2.1.js"></script>
<script src="${context_path}/resources/js/game.js"></script>
<script src="${context_path}/resources/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href="${context_path}/resources/css/game.css"></link>
<link rel="stylesheet" type="text/css" href="${context_path}/resources/css/bootstrap.min.css"></link>
<title>Insert title here</title>
</head>
<body>

	

	<div class="img">
		<div class="bot_ui">
			<div class="name">AI Bot</div>
			<div class="text">Score: <div class="score">-1</div></div>
			<img src="${context_path}/resources/img/gawi.png"/><div class="game gawi"></div>
			<img src="${context_path}/resources/img/bawi.png"/><div class="game bawi"></div>
			<img src="${context_path}/resources/img/bo.png"/><div class="game bo"></div>
		
		</div>
		
		
		
		<div class="user_ui"> 
			<div class="name">Kaiji</div>
			<img src="${context_path}/resources/img/gawi.png" onclick="Game.doGame(0);"/><div class="game gawi"></div>
			<img src="${context_path}/resources/img/bawi.png" onclick="Game.doGame(1);"/><div class="game bawi"></div>
			<img src="${context_path}/resources/img/bo.png"   onclick="Game.doGame(2);"/><div class="game bo"></div>
			<div class="text">Score: <div class="score">-1</div></div>
		</div>
	</div>
	
	<div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-sm">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Modal Header</h4>
        </div>
        <div class="modal-body">
          <p>This is a large modal.</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>


</body>
</html>