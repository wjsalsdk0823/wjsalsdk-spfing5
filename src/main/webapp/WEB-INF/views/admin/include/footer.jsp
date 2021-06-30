<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 이후 footer.jsp 영역 -->
  <footer class="main-footer">
    <strong>Copyright &copy; 2014-2021 <a href="https://adminlte.io">AdminLTE.io</a>.</strong>
    All rights reserved.
    <div class="float-right d-none d-sm-inline-block">
      <b>Version</b> 3.1.0
    </div>
  </footer>

  <!-- Control Sidebar 오른쪽 바둑판 메뉴 클릭시 나오는 내용-->
  <aside class="control-sidebar control-sidebar-dark">
    <!-- demo.js 에서 출력할 내용이 존재, jemo.js사용안함 -->
    <div class="text-center mt-4">
    <h5>로그아웃</h5><hr class="mb-2"/>
    <button type="button" class="btn btn-primary" id="btn_logout">로그아웃</button>
    </div>
  </aside>
  <!-- /.control-sidebar -->
</div>
<!-- ./wrapper -->

<!-- jQuery 제이쿼리 코어-->
<script src="/resources/admin/plugins/jquery/jquery.min.js"></script>
<!-- jQuery UI 1.11.4 -->
<script src="/resources/admin/plugins/jquery-ui/jquery-ui.min.js"></script>
<!-- Resolve conflict in jQuery UI tooltip with Bootstrap tooltip -->
<script>
  $.widget.bridge('uibutton', $.ui.button)
</script>
<!-- Bootstrap 4 부트스트랩4 코어-->
<script src="/resources/admin/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<!-- Summernote 섬머노트웹에디터 코어 -->
<script src="/resources/admin/plugins/summernote/summernote-bs4.min.js"></script>
<!-- overlayScrollbars 왼쪽스크롤메뉴 코어 -->
<script src="/resources/admin/plugins/overlayScrollbars/js/jquery.overlayScrollbars.min.js"></script>
<!-- AdminLTE App AdminLTE코어 -->
<script src="/resources/admin/dist/js/adminlte.js"></script>
<!-- AdminLTE for demo purposes 오른쪽메뉴 로그아웃사용 코어:사용안함-->
<!-- <script src="/resources/admin/dist/js/demo.js"></script>/ -->
</body>
</html>
<style>
.sidebar-dark-primary .nav-sidebar>.nav-item>.nav-link.active, .sidebar-light-primary .nav-sidebar>.nav-item>.nav-link.active
{
background-color:#fff;
color:#000;
}
</style>
<script>
//왼쪽메뉴 선택시 active부트스트랩 클래스를 동적으로 추가하는 코드 아래
$(document).ready(function(){
	var current = location.pathname;
	//alert(current);
	var current2 = current.split("/")[2];
	//alert(current2);
	$(".nav-treeview li a").each(function(){
		if($(this).attr('href').indexOf(current2) != -1) {
			if(current2 != "board") {
				$(this).addClass("active");
			}			
		}else{
			$(this).removeClass("active");
		}
	});
});
</script>