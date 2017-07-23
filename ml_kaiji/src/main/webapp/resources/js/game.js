/**
 * 
 */
var Game = {
	
	// 봇 기준이라 my가 봇이다. 데헷!
	myDeck : []
	, emDeck : []
	, myWin : 0
	, emWin : 0
	, count : 0
	
	, makeChoiceUrl : "/kaiji/api/makeChoice"
	
	, initGame : function(){
		
		this.myDeck = [3, 3, 3];
		this.emDeck = [3, 3, 3];
		this.myWin = 0;
		this.emWin = 0;
		this.count = 0;
		
		this.render();
		
	}
	// 현재 상황을 object 로 리턴 시킨다.
	, getState : function()
	{
		var rt = {};
		
		rt.myDeck = this.myDeck;
		rt.emDeck = this.emDeck;
		rt.myWin = this.myWin;
		rt.emWin = this.emWin;
		rt.count = this.count;
		
		return rt;
	}
	, doGame: function(action)
	{
		userValid = this.makeUserChoice(action);
		if(!userValid)
		{
			return false;
		}
			
		
		botAction = this.makeBotChoice();
		
		
		if(action >= 0 )
		{
			this.emDeck[action]-=1;
		}
		
		if(action >= 0 )
		{
			this.myDeck[botAction]-=1;
		}
		
		
		// 0은 가위
		if(action == 0)
		{
			if(botAction == 0)
			{}
			else if(botAction == 1)
			{
				this.myWin++;
			}
			else if(botAction == 2)
			{
				this.emWin++;
			}
		}
		// 1은 바위
		else if (action == 1)
		{
			if(botAction == 0)
			{
				this.emWin++;
			}
			else if(botAction == 1)
			{}
			else if(botAction == 2)
			{
				this.myWin++;
			}
		}
		// 2는 보자기
		else if (action == 2)
		{
			if(botAction == 0)
			{
				this.myWin++;
			}
			else if(botAction == 1)
			{
				this.emWin++;
			}
			else if(botAction == 2)
			{}
		}
		
		this.count++;
		
		if(this.count >= 9 || this.myWin >= 5 || this.emWin >= 5)
		{
			this.endGame();
		}
		this.render();
	}
	, endGame : function()
	{
		alert("게임 끝났다.");
		this.initGame();
	}

	, makeUserChoice : function(action)
	{
		alert(action);
		if (action < 0 || action >= this.emDeck.length)
		{
			alert("잘못된 선택입니다.");
			return false;
		}
			
		if(this.emDeck[action] == 0)
		{
			alert("잘못된 선택입니다.");
			return false;
		}
		
			
		return true;
	}

	
	, makeBotChoice: function()
	{
		var data = this.getState();
		var action = -1;
		
		$.ajax({
		    type: 'POST',
		    url: this.makeChoiceUrl,
		    data: JSON.stringify( data ),
		    async: false, 
		    success: function(data) { 
		    	try
		    	{action = data.recv.action; }
		    	catch(e)
		    	{action = -1;}
		    },
		    contentType: "application/json",
		    dataType: 'json'
		});

		
		
		return action;
	}

	// 화면을 그리는 뭐시기가 여기 들어 가겠지 모
	, render : function()
	{
		$(".bot_ui").find(".gawi").text(this.myDeck[0]);
		$(".bot_ui").find(".bawi").text(this.myDeck[1]);
		$(".bot_ui").find(".bo").text(this.myDeck[2]);
		$(".bot_ui").find(".score").text(this.myWin);
		
		$(".user_ui").find(".gawi").text(this.emDeck[0]);
		$(".user_ui").find(".bawi").text(this.emDeck[1]);
		$(".user_ui").find(".bo").text(this.emDeck[2]);
		$(".user_ui").find(".score").text(this.emWin);
	}
};


$(document).ready(function(){
	Game.initGame();
});