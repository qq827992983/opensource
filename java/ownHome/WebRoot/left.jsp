<%@ page language="java" pageEncoding="gb2312"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table width="183" border="0" cellpadding="0" cellspacing="0"
	background="images/bg07.gif">
	<tr>
		<td><img src="images/bg06.gif" width="183" height="43"></td>
	</tr>
	<tr>
		<td height="157" valign="top">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>

				<td height="62" align="center">
				<FORM name="register" method="post" action="LoginServlet">
				<table width="90%" border="0" cellspacing="0" cellpadding="0">
					<c:set var="user" value="${sessionScope.user }" scope="session" />
					<c:if test="${user==null }">
						<tr>
							<td width="12%" align="right"><img src="images/dot01.gif"
								width="5" height="7"></td>
							<td width="32%" align="right">�û�����</td>
							<td width="56%"><input name="userName" type="text"
								class="form" size="12"></td>
						</tr>
						<tr>
							<td align="right"><img src="images/dot01.gif" width="5"
								height="7"></td>
							<td align="right">��&nbsp;&nbsp;�룺</td>
							<td><input name="password" type="password" class="form"
								size="12"></td>
						</tr>
						<tr align="center">
							<td height="36" colspan="3"><input name="imageField"
								type="image" src="images/button01.gif" width="60" height="19"
								border="0"> &nbsp; <a href="register.jsp"><img
								src="images/button02.gif" width="60" height="19" border="0">
							</a></td>
						</tr>
					</c:if>
					<c:if test="${user!=null }">
						<tr>
							<td width="12%" align="right"><img src="images/dot01.gif"
								width="5" height="7"></td>
							<td width="32%" align="right">���ã�</td>
							<td width="56%">${user.userName }</td>
						</tr>
						<tr>
							<td align="right"><img src="images/dot01.gif" width="5"
								height="7"></td>
							<td align="center" colspan="2">
							��ӭ��������&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>

						</tr>
						<tr align="center">
							<td height="36" colspan="3"><a href="InvalidateServlet"><img
								src="images/button05.jpg" width="60" height="19" border="0" /></a>
							</td>
						</tr>
					</c:if>

				</table>
				</FORM>
				</td>

			</tr>
			<tr>
				<td align="center"><img src="images/line01.gif" width="157"
					height="1"></td>
			</tr>
			<tr>
				<td height="29" valign="bottom"><img src="images/left01.jpg"
					width="183" height="15"></td>
			</tr>
			<tr>
				<td height="106" align="center" valign="top">
				<FORM name="form2" method="post" action="SearchServlet">
				<table width="90%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td colspan="2" height=5></td>
					</tr>
					<tr align="center">
						<td width="27%">��&nbsp;��</td>
						<td width="73%"><select name="type" style="width:85px"
							class="form">
							<option value=""></option>
							<option value="1">JAVA����</option>
							<option value="2">#.NET����</option>
							<option value="3">�����ռ�</option>
							<option value="4">��־����</option>

						</select></td>
					</tr>
					<tr align="center">
						<td>��&nbsp;�⣺</td>
						<td><input type="text" name="title" size="12" class="form" />

						</td>
					</tr>

					<tr align="center">
						<td>��&nbsp;�ߣ�</td>
						<td><input type="text" name="writer" size="12" class="form" />
						</td>
					</tr>
					<tr valign="bottom" align="center">
						<td height="30" colspan="2">&nbsp; <input name="imageField2"
							type="image" src="images/button03.gif" width="60" height="19"
							border="0"></td>
					</tr>
				</table>
				</FORM>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td valign="bottom"><img src="images/line02.gif" width="183"
			height="19"></td>
	</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td height=8></td>
	</tr>
</table>
<table width="100%" border="0" cellpadding="0" cellspacing="0"
	background="images/bg08.gif">
	<tr>
		<td><img src="images/left02.gif" width="183" height="39"></td>
	</tr>
	<tr>
		<td height="60" align="center" valign="top">
		<table width="90%" border="0" cellspacing="0" cellpadding="3">
			<tr height="18">
				<td><script language="JavaScript"> 
document.write('<marquee style="color:#ff0000;font-size:12px;line-height:17px;" '
+ ' direction="up" height="100" scrollamount="1" scrolldelay="100" ' 
+ ' onMouseOver="this.scrollDelay=5000" onMouseOut="this.scrollDelay=1"> ' 
+ ' ����ؼ䣬<br />����ÿһ���˶���<br />��һ�޶���<br />���ڲ�ͬ�ġ�<br /> '
+ ' ֻҪ�����Լ���<br />ֻҪ��Ŭ����<br />�ͻ�������á�<br />  ' 
+ ' ��֣� <br /> �ɹ������㣡 '
+ '</marquee>'); 
</script></td>
			</tr>

		</table>
		</td>
	</tr>
	<tr>
		<td><img src="images/line02.gif" width="183" height="20"></td>
	</tr>
</table>
