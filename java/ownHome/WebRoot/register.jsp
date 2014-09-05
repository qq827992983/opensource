<%@ page language="java" pageEncoding="gb2312"%>
<HTML>
<HEAD>
<TITLE>E-学习</TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
<link href="css.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY BGCOLOR=#FFFFFF LEFTMARGIN=0 TOPMARGIN=0 MARGINWIDTH=0
	MARGINHEIGHT=0>
<!--   top.html -->
<%@ include file="top.html"%>
<!--  top.html end -->
<table width="778" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height=5 colspan="3" bgcolor="eeeeee"></td>
	</tr>
	<tr>
		<td width="9" height=15 bgcolor="#FFFFFF"></td>
		<td width="714" background="images/bg04.gif" bgcolor="#FFFFFF"></td>
		<td width="8" bgcolor="#FFFFFF"></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td colspan="3" height=5></td>
	</tr>
</table>

<table width="778" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td width="7" height="461" valign="top" bgcolor="#FFFFFF"><img
			src="images/bg05.gif" width="7" height="8"></td>
		<td width="170" valign="top"><!-- left.jsp --> <%@ include
			file="left.jsp"%> <!--  left.jsp  --> <br>
		</td>
		<td width="595" align="right" valign="top"><!-- banner.html --> <%@ include
			file="banner.jsp"%> <!-- banner.html  end  -->

		<!-- main.jsp -->

<FORM name="register" method="post" action="RegisterServlet">
		<table width="570"  border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="570" height="100" valign="top">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">					
					<tr>
						<td valign="bottom" colspan="2"><img src="images/cen02.gif"
							width="95%" height="4"></td>
					</tr>
					<tr>
						<td align="center" colspan="2">
						<table width="90%" height="250" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td align="right">用户名：</td>
								<td align="center"><input type="text" name="userName" /></td>
							</tr>
							<tr>
								<td align="right">密&nbsp;&nbsp;码：</td>
								<td align="center"><input type="password" name="password" /></td>
							</tr>
							<tr>
								<td align="right">确认密码：</td>
								<td align="center"><input type="password" name="password2" /></td>
							</tr>
							<tr>
								<td align="right">真实姓名：</td>
								<td align="center"><input type="text" name="realName" /></td>
							</tr>
							<tr>
								<td align="right">性别：</td>
								<td align="center"><input type="radio" name="sex" value="m"/>男
								<input type="radio" name="sex" value="f"/>女</td>
							</tr>
							<tr>
								<td align="right"><input  type="image" src="images/button06.jpg"
										width="60" height="19" border="0"></td>
								<td align="center"><input type="image" src="images/button07.jpg"
										width="60" height="19" border="0" /></td>
							</tr>
						</table>
						</td>
					</tr>
					<tr>
						<td valign="bottom" colspan="2"><img src="images/cen02.gif"
							width="95%" height="4"></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
</FORM>		
		
		<!--main.jsp  end --></td>
		<td width="6" align="right" valign="top"><img
			src="images/bg05.gif" width="6" height="1"></td>
	</tr>
</table>
<table width="778" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td width="9" height=15 bgcolor="#FFFFFF"></td>
		<td width="714" background="images/bg04.gif" bgcolor="#FFFFFF"></td>
		<td width="8" bgcolor="#FFFFFF"></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td colspan="3" height=2></td>
	</tr>
</table>
<TABLE WIDTH=778 BORDER=0 align="center" CELLPADDING=0 CELLSPACING=0>
	<TR>
		<TD height="73" align="center" background="images/bg13.gif">版权所有：E-学习<br>
		Copyright love.pesms.com All Rights Reserved.</TD>
	</TR>
</TABLE>
</BODY>