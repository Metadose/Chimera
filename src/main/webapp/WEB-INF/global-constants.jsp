<%
String HOST_NAME = request.getServerName();
int HOST_PORT = request.getServerPort();
String CONTEXT_PATH = request.getContextPath();

// TODO Don't hardcode "http://" 
String ABSOLUTE_PATH = "http://" + HOST_NAME + ":" + HOST_PORT + CONTEXT_PATH + "/";
%>