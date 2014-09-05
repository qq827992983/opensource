<%@ page language="java" pageEncoding="gb2312"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="list1" value="${sessionScope.list1}" scope="session" />
<c:set var="list2" value="${sessionScope.list2}" scope="session" />
<c:set var="list3" value="${sessionScope.list3}" scope="session" />
<c:set var="list4" value="${sessionScope.list4}" scope="session" />

<table width="570" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="289" height="100" valign="top">
		<table width="272" border="0" cellpadding="0" cellspacing="0"
			background="images/bg11.gif">
			<tr>
				<td><img src="images/cen01.jpg" width="272" height="37"></td>
			</tr>
			<tr>
				<td height="83" align="center">
				<table width="90%" border="0" cellspacing="0" cellpadding="0">
					<c:forEach var="article1" items="${list1 }">
						<tr>
							<td align="left"><a href="DetailArticleServlet?articleId=${article1.articleId }">${article1.title }</a></td>
							<td align="right"><span class="date">[${article1.writeDate}]</span></td>
						</tr>   
					</c:forEach>
				</table>
				</td>
			</tr>
			<tr>
				<td valign="bottom"><img src="images/cen02.gif" width="272"
					height="4"></td>
			</tr>
		</table>
		</td>
		<td width="289" align="right" valign="top">
		<table width="272" border="0" cellpadding="0" cellspacing="0"
			background="images/bg12.gif">
			<tr>
				<td><img src="images/cen02.jpg" width="279" height="37"></td>
			</tr>
			<tr>
				<td height="81" align="center">
				<table width="90%" border="0" cellspacing="0" cellpadding="0">
					<c:forEach var="article2" items="${list2 }">
						<tr>
							<td align="left"><a href="DetailArticleServlet?articleId=${article2.articleId }">${article2.title }</a></td>
							<td align="right"><span class="date">[${article2.writeDate}]</span></td>
						</tr>   
					</c:forEach>
				</table>
				</td>
			</tr>
			<tr>
				<td height="4" valign="bottom"><img src="images/cen04.gif"
					width="279" height="4"></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td width="289" height="100" valign="top">
		<table width="272" border="0" cellpadding="0" cellspacing="0"
			background="images/bg11.gif">
			<tr>
				<td><img src="images/cen03.jpg" width="272" height="37"></td>
			</tr>
			<tr>
				<td height="83" align="center">
				<table width="90%" border="0" cellspacing="0" cellpadding="0">
					<c:forEach var="article3" items="${list3 }">
						<tr>
							<td align="left"><a href="DetailArticleServlet?articleId=${article3.articleId }">${article3.title }</a></td>
							<td align="right"><span class="date">[${article3.writeDate}]</span></td>
						</tr>   
					</c:forEach>

				</table>
				</td>
			</tr>
			<tr>
				<td valign="bottom"><img src="images/cen02.gif" width="272"
					height="4"></td>
			</tr>
		</table>
		</td>
		<td width="289" align="right" valign="top">
		<table width="272" border="0" cellpadding="0" cellspacing="0"
			background="images/bg12.gif">
			<tr>
				<td><img src="images/cen04.jpg" width="279" height="37"></td>
			</tr>
			<tr>
				<td height="81" align="center">
				<table width="90%" border="0" cellspacing="0" cellpadding="0">
					<c:forEach var="article4" items="${list4 }">
						<tr>
							<td align="left"><a href="DetailArticleServlet?articleId=${article4.articleId }">${article4.title }</a></td>
							<td align="right"><span class="date">[${article4.writeDate}]</span></td>
						</tr>   
					</c:forEach>

				</table>
				</td>
			</tr>
			<tr>
				<td height="4" valign="bottom"><img src="images/cen04.gif"
					width="279" height="4"></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
