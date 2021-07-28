<%@ include file="/init.jsp"%>

<div class="application-list card card-body h-100">
	<div class="text-center">
		<div class="container">
			<div class="row align-items-center justify-content-center pt-3">
				<c:forEach var="app" items="${apps}">
					<div class="col p-0 m-0">
						<div class="mini-card text-center">
							<a href="${app.link}" class="btn"> <i class="icon icon-3x ${app.icon}" />
								<br /> ${app.title}
							</a>
						</div>
					</div>
				</c:forEach>
				<c:if test="${empty apps}">
					<div class="mini-card text-center">
						No apps selected
					</div>
				</c:if>
			</div>
		</div>
	</div>
</div>