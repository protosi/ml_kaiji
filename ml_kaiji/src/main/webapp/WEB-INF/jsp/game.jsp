<%@page import="java.io.Writer"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="${context_path}/resources/js/jquery-3.2.1.js"></script>
<script src="${context_path}/resources/js/game.js"></script>
<link rel="stylesheet" type="text/css" href="${context_path}/resources/css/game.css"></link>
<title>Insert title here</title>
</head>
<body>

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
	

</body>
</html>