<!DOCTYPE html>
<html>
<head>
<title>T21331 Webapplicaties: de serverkant – Practicumopdracht 1</title></head>
<link rel="stylesheet" type="text/css" href="./css/main.css">
<script type="text/javascript">var sessionid = "<%=session.getId()%>";</script>
<script type="text/javascript" src="./js/main.js"></script> 
<body>
<div class="grid column">
	<div id="vakje0" class="vakje"></div>
	<div id="vakje1" class="vakje"></div>
	<div id="vakje2" class="vakje"></div>
	<div id="vakje3" class="vakje"></div>
	<div id="vakje4" class="vakje"></div>
	<div id="vakje5" class="vakje"></div>
	<div id="vakje6" class="vakje"></div>
	<div id="vakje7" class="vakje"></div>
	<div id="vakje8" class="vakje"></div>
</div>
<div class="column">	
  <div><button id="reset">Nieuw spel</button></div>
  <div>
    <button id="slim" class="buttonselect">Computer speelt slim</button>
    <button id="dom" class="buttonunselect">Computer speelt dom</button>
  </div>
  <div id="computerwint" class="hide">Computer wint</div>
  <div id="spelerwint" class="hide">Speler wint</div>
  <div id="gelijk" class="hide">Gelijk</div>
</div>
</body>
</html>