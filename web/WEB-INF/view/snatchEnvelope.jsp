<%--
  Created by IntelliJ IDEA.
  User: makris
  Date: 2018/2/25
  Time: 上午10:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html"; charset="UTF-8">
    <title>Title</title>

    <script type="text/javascript" src="https://code.jquery.com/jquery-3.2.0.js"></script>

    <script>
        $(document).ready(function(){
           var max = 30000;
           for (var i = 1; i <= max; i++){
               $.post({
                   url: "./snatchEnvelope?envelopeId=1&userId=" + i,
                   success: function (result) {
                       console.log("搶紅包結果");
                       console.log(result);
                   }
               });
           }
        });
    </script>
</head>
<body>
</body>
</html>
