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


		<table width="570"  border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="570" height="100" valign="top">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">					
					<tr>
						<td valign="bottom" colspan="2"><img src="images/cen02.gif"
							width="95%" height="4"></td>
					</tr>
					<tr>
						<td height="83" align="center" colspan="2">
						<table width="90%" height="250" border="0" cellspacing="0" cellpadding="0">
							<c:set var="article" value="${requestScope.detailArticle }" />
							<tr>
								<td align="center">${article.title }</td>
							</tr>
							<tr>
								<td align="right">作者：${article.writer }</td>
							</tr>
							<tr>
								<td align="left">${article.content }</td>
							</tr>
							<tr>
								<td align="right">写于：${article.writeDate }</td>
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