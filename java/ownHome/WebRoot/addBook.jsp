<%@ page language="java" pageEncoding="gb2312"%>
<HTML>
<HEAD>
<TITLE>E-ѧϰ</TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
<link href="css.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
function checkForm(param){
	var content = document.getElementById(param).value;
	if(content==null || content==""){
		alert("'����'������Ϊ�գ����������룡");
		return false;
	}else{
		submitForm();
	}	
}
function submitForm(){
	document.addBook.action="AddBookServlet";
	document.addBook.submit();
}
</script>
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


		<table width="570" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="570" height="100" valign="top">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td valign="bottom" colspan="2"><img src="images/cen02.gif"
							width="95%" height="4"></td>
					</tr>
					<tr>
						<td height="83" align="center" colspan="2">
						<FORM name="addBook" method="post" action="AddBookServlet">
						<TABLE width="100%"
							style="cellpadding: 0px; cellspacing: 0px; margin-top: 0px; margin-Left: 0px"
							style="table-layout: fixed;WORD-BREAK: break-all; WORD-WRAP: break-word">
							<TR>
								<TD style="color:#4c4743;line-height:160%;" valign="top"
									width="20%">������</TD>
								<TD style="color:#4c4743;line-height:160%;" valign="top"
									width="30%"><input type="text" id="name" name="name"
									alt="����" size="15" /></TD>

								<TD style="color:#4c4743;line-height:160%;" valign="top"
									width="20%">�Ա�</TD>
								<TD style="color:#4c4743;line-height:160%;" valign="top"
									width="30%"><select name="sex">
																	<option value="��">��</option>
																	<option value="Ů">Ů</option>
																</select></TD>
							</TR>
							<TR>
								<TD style="color:#4c4743;line-height:160%;" valign="top"
									width="30%">�绰��</TD>
								<TD style="color:#4c4743;line-height:160%;" valign="top"><input
									type="text" name="phone" size="15" /></TD>

								<TD style="color:#4c4743;line-height:160%;" valign="top"
									width="30%">��ַ��</TD>
								<TD style="color:#4c4743;line-height:160%;" valign="top"><input
									type="text" name="address" size="15" /></TD>
							</TR>
							<TR>
								<TD style="color:#4c4743;line-height:160%;" valign="top"
									width="30%">�ֻ���</TD>
								<TD style="color:#4c4743;line-height:160%;" valign="top"><input
									type="text" name="mobilePhone" size="15" /></TD>

								<TD style="color:#4c4743;line-height:160%;" valign="top"
									width="30%">��˾���ƣ�</TD>
								<TD style="color:#4c4743;line-height:160%;" valign="top"><input
									type="text" name="company" size="15" /></TD>
							</TR>
							<TR>
								<TD style="color:#4c4743;line-height:160%;" valign="top"
									width="30%">��˾�绰��</TD>
								<TD style="color:#4c4743;line-height:160%;" valign="top"><input
									type="text" name="comPhone" size="15" /></TD>

								<TD style="color:#4c4743;line-height:160%;" valign="top"
									width="30%">��˾��ַ��</TD>
								<TD style="color:#4c4743;line-height:160%;" valign="top"><input
									type="text" name="comAddress" size="15" /></TD>
							</TR>
							<TR>
								<TD style="color:#4c4743;line-height:160%;" valign="top"
									width="30%">��ϵ��</TD>
								<TD style="color:#4c4743;line-height:160%;" valign="top"><select name="relation">
																	<option value="1">����</option>
																	<option value="2">�쵼</option>
																	<option value="3">ʦ��</option>
																	<option value="4">����</option>
																	<option value="5">ͬѧ</option>
																	<option value="6">ͬ��</option>
																</select></TD>
							</TR>
							<TR>
								<td align="right">&nbsp;</td>
								<TD style="color:#4c4743;line-height:160%;" valign="top"
									width="30%"><input type="button" value="�ύ"
									onClick="checkForm('name')" /></TD>
								<TD style="color:#4c4743;line-height:160%;" valign="top"><input
									type="reset" value="����" /></TD>
							</TR>
						</TABLE>
						</FORM>
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
		<TD height="73" align="center" background="images/bg13.gif">��Ȩ���У�E-��԰<br>
		Copyright love.pesms.com All Rights Reserved.</TD>
	</TR>
</TABLE>
</BODY>