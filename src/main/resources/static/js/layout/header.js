/**
 * header 영역 메뉴 불러오기
 */
$(document).ready(function() {
    userHeader();
    renderMenu();
});


// 헤더 영역 사용자 이름 표시
function userHeader(){
	$.get("/api/status", function (data) {
        if (data.status == 200) {
            $("#loginStatus").text("로그아웃");
            $("#loginLink").attr("onclick", "logout()");
        } else {
            $("#loginStatus").text("로그인");
            $("#loginLink").attr("href", "/login");
        }
    });

}

function renderMenu(){

    // AJAX Url
    var url = '/api/parent-menu';
    // AJAX GET
    mz.util.ajaxObj(url,{},'GET').done(function(data) {

		data = data.data;

        let $navList = $(".nav-list");
        let menuMap = {}; // 메뉴를 parentMenuNo 기준으로 매핑

        data.forEach(function(menu) {
            menuMap[menu.menuNo] = menu;
        });

        data.forEach(function(menu) {

            if ( menu.menuLv === 2 ) {
                // 2레벨 메뉴는 li로 바로 추가
                var $li = $("<li>").addClass("list");
                var $a = $("<a>").attr("href", menu.url).text(menu.menuNm).attr("title", "페이지 이동");
                $li.append($a);
                $navList.append($li);

            } else if ( menu.menuLv === 3 ) {
                // 3레벨 메뉴는 상위 메뉴를 찾아서 ul.dep02 안에 추가
                var parentMenu = menuMap[menu.parentMenuNo];
                if (parentMenu) {
                    // 부모 메뉴가 존재하는 경우 ul.dep02를 찾거나 생성
                    var $parentLi = $navList.find("a:contains('" + parentMenu.menuNm + "')").closest("li");
                    var $dep02 = $parentLi.find("ul.dep02");

                    if ($dep02.length === 0) {
                        $dep02 = $("<ul>").addClass("dep02");
                        $parentLi.append($dep02);
                    }

                    // li 추가
                    var $li = $("<li>");
                    var $a = $("<a>").attr("href", menu.url).text(menu.menuNm).attr("title", "페이지 이동");
                    $li.append($a);
                    $dep02.append($li);
                }
            }
        });
    });
}


function haderMenuEvent( targetTag, menuNo ){

    mz.util.addCookie('movePage','Y');
    $(targetTag).addClass('gnb_item--active');
    // 헤더영역 클릭 flag
    var parentFlag = 'Y';

    mz.util.loadSubMenu( menuNo, parentFlag );
}


// 로그아웃 function
function logout(){
    mz.util.ajaxObj("/api/logout").done(function(data){
        window.localStorage.removeItem("name");
		window.localStorage.removeItem("sysId");
        location.href = "/login";
    }).fail(function(error){
        console.log(error);
        location.href = "/index";
    });
}