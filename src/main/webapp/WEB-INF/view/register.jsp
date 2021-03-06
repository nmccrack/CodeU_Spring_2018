<!DOCTYPE html>
<html>
<head>
  <title>Register</title>
  <link rel="stylesheet" href="/css/main.css">
  <style>
    label {
      display: inline-block;
      width: 100px;
    }
  </style>
</head>
<body>

  <nav>
    <a id="navTitle" href="/">CodeU Chat App</a>
    <a href="/conversations">Conversations</a>
    <a href="/searchResult">Search</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a>Hello <%=request.getSession().getAttribute("user") %> !</a>
    <% }else{ %>
      <a href="login">Login</a>
      <a href="/register">Register</a>
    <% } %>
     <a href="/about.jsp">About</a>
     <a href="/activity_feed">Activity Feed</a>
  </nav>

  <div id="container">
    <h1>Register</h1>

    <% if(request.getAttribute("error") != null){ %>
       <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>

    <form action="/register" method="POST">
      <label for="username">Username: </label>
      <input type="text" name="username" id="username">
      <br/>
      <label for="password">Password: </label>
      <input type="password" name="password" id="password">
      <br/>
      <label for="admin">Admin: </label>
      <select name="admin">
        <option value="true"> Yes </option>
        <option value="false"> No </option>
      </select>
      <br/>
      <input type="checkbox" onclick="hidePassword()">hide/show password
      <script type="text/javascript">
        function hidePassword(){
          var passwordAtt = document.getElementById('password');
          if (passwordAtt.type == "password"){
            passwordAtt.type = 'text';
          } else {
            passwordAtt.type = 'password';
          }
        }
      </script>
      <br/><br/>
      <button type="submit">Submit</button>
      <button type="reset">Reset</button>
    </form>
  </div>
</body>
</html>
