<!DOCTYPE html>
<html>
<head><title>My first JSP</title></head>
<body>
<% 
    String someText = request.getParameter("sometext");
%>

<h1>And here is the output</h1>

<p>
  <%= someText %>
</p>

</body>
</html>