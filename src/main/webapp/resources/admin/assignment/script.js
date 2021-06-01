//innerText ->element 안의 text 값들만을 가져옵니다.
//innerHTML -> innerText와는 달리 innerHTML은 element 안의 HTML이나 XML을 가져옵니다.
function forclear() { //c 버튼 초기화 기능 함수
    document.getElementById("output").innerHTML = "0";
}
function removeZero() { //계산결과 출력시 맨 앞의 0제거 함수
    var value = document.getElementById("output").innerHTML;
    if (value == "0") {
        value = " "
        document.getElementById("output").innerHTML = value;
    }
}
function perc() { //백분율(퍼센트) 계산 함수
    var value = document.getElementById("output").innerHTML;
    value = value / 100;
    document.getElementById("output").innerHTML = value;
}
function fordisplay(value) { // 버튼 클릭한 숫자가 이어서 출력되게 하는 함수
    removeZero()
    document.getElementById("output").innerHTML +=value;//+=오른쪽:우측의 연산자의 값을 변수에 더하는 연산자
}
function solve() { //계산식 작성 후 = 버튼을 클릭 했을 때 결과를 출력하는 함수
    removeZero()
    var equation = document.getElementById("output").innerHTML;
    var solved = eval(equation);
    document.getElementById('output').innerHTML = solved;
}