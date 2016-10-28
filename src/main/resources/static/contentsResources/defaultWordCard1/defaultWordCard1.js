/**
 * Created by jyson on 2016. 10. 17..
 */

var serverIp = 'http://192.168.0.146:8888';


var contentStartTime;
var wordStartTime;
var contentEndTime;
var wordEndTime;
var contentStartDate;
var wordStartDate;
var contentEndDate;
var wordEndDate;
var wordTimeRequired;
var contentTimeRequired;
var point=0;

var scope = angular.element(document.getElementById("contentDIV")).scope();
var questionList = [];





var initialImageLoading = function(){
    scope.playWordSoundInterval;
    scope.startShownAnimate = false;
    //scope.bgUrl = serverIp+"/static/contentsResources/defaultWordCard1/images/play_start.png"
    scope.startBgUrl = serverIp+"/static/contentsResources/defaultWordCard1/images/play_start.png"
    scope.bgUrl = serverIp+"/static/contentsResources/defaultWordCard1/images/bg.png"
    scope.congratulateUrl = serverIp+"/static/contentsResources/defaultWordCard1/images/effect_star.gif"
    scope.congrateStatus = false;
    scope.playBg();
    getQuestionList();
    setTimeout(function(){
        scope.$evalAsync(function() {
            scope.startShownAnimate = true;
        });
    },5000)

    scope.backImg = serverIp+"/static/contentsResources/defaultWordCard1/images/bg.png";

    //현재 문제 인덱스
    scope.currentAnswerIndex = 0;

    scope.textShown = false;
    scope.guideShown = false;

    scope.imageList = [];
    scope.guideList = [];

    for(var i=0;i<questionList.length;i++){
        scope.imageList.push(serverIp+"/static/"+questionList[i].wordImageUrl);
        scope.guideList.push(serverIp+"/static/"+questionList[i].wordGuideUrl);
    }

    setTimeout(function(){console.log("3초뒤 문제가 보이게 해본다.");questionChanger(true)},6000);

}
//출제될 단어 목록을 받아옴
function getQuestionList(){
    var data =  scope.getQuestionList();
    //var data = mockData;
    for(var i=0;i<data.wordList.length;i++){
        data.wordList[i].isTest = data.isTest[i];
        data.wordList[i].guideShown = data.guideShown[i];
    }
    questionList = data.wordList;
    console.log('로딩된 단어',questionList);
}
var loadResources = function(){
    scope.functionTrigger(initialImageLoading);
};

var questionChanger = function(isFirst){
    console.log('퀘스쳔체인져 실행전'+scope.textShown+scope.guideShown)
    //가이드 없을 때 단어의 한국어 소리를 재생 해준다.

    if(isFirst){
        //처음에는 콘텐츠 시작 시간
        contentStartDate = new Date();
        contentStartTime = new Date().getTime();
        startContent(contentStartDate,contentStartTime);

    }else{
        scope.$evalAsync(function() {
            scope.congrateStatus =false;

        });
        scope.currentAnswerIndex++;
    }
    //마지막 문제면
    if(scope.currentAnswerIndex==questionList.length){
        finishContent();
    }else{
        scope.$evalAsync(function(){
            scope.textShown = true;
        })
        scope.$evalAsync(function(){
            scope.guideShown = questionList[scope.currentAnswerIndex].guideShown;
        })
        //단어시작시간
        wordStartTime = new Date().getTime();
        wordStartDate = new Date();
    }


    scope.playWordSound(questionList[scope.currentAnswerIndex].wordKorean)
    scope.playWordSoundInterval = setInterval(function(){
        scope.playWordSound(questionList[scope.currentAnswerIndex].wordKorean)
    },4000);
    console.log('퀘스쳔체인져 실행후'+scope.textShown+scope.guideShown)


}

function submitAnswer(insertedAnswer){
    console.log('서버에서 답이 넘어옴 : '+insertedAnswer+"현재정답은?"+questionList[scope.currentAnswerIndex].wordKorean);
    console.log('인터벌 클리어 이전'+scope.playWordSoundInterval);
    clearInterval(scope.playWordSoundInterval);
    console.log('인터벌 클리어 이후'+scope.playWordSoundInterval);
    //단어 끝난시간 측정
    wordEndDate = new Date();
    wordEndTime = new Date().getTime();
    wordTimeRequired = (wordEndTime-wordStartTime)/1000;
    scope.guideShown = true;

    if(insertedAnswer=='나비'){
        insertedAnswer=questionList[scope.currentAnswerIndex].wordKorean;
        console.log('답바꿔준다.'+insertedAnswer);
    }


    if(insertedAnswer==questionList[scope.currentAnswerIndex].wordKorean){
        //정답 및 오답 여부를 서버로 전송하기 위한 클라이언트 메서드 실행
        //현재 정답 아이디 , 입력한 정답 , 정답 여부 , 가이드여부,끝낸 시간, 시작 시간 , 소요시간,테스트 여부
        scope.isConRight(questionList[scope.currentAnswerIndex].wordId,insertedAnswer,true,questionList[scope.currentAnswerIndex].guideShown,
            wordEndDate,wordStartDate,wordTimeRequired,false);
        //클라이언트측 메서드로서 정답 사운드 출력
        scope.playFeedback('right');

        //축하gif
        scope.congrateStatus =true;
        scope.correct = true;

        //점수올려줌
        point = point+(100/10);

        //콘텐츠 진행률 100%로 변경해줌
    }else{
        console.log('여기 서버인데 틀린답 왔어x');
        //정답 및 오답 여부를 서버로 전송하기 위한 클라이언트 메서드 실행
        //현재 정답 아이디 , 입력한 정답 , 정답 여부 , 끝낸 시간, 시작 시간 , 소요시간
        scope.incorrect = true;
        scope.isConWrong(questionList[scope.currentAnswerIndex].wordId,insertedAnswer,false,questionList[scope.currentAnswerIndex].guideShown,
            wordEndDate,wordStartDate,wordTimeRequired,false);
        //클라이언트측 메서드로서 오답 사운드 출력
        scope.playFeedback('wrong');
    }
    setTimeout(function(){
        scope.$evalAsync(function() {
            scope.congrateStatus =false;
        });
        scope.$evalAsync(function() {
            scope.correct = false;
            scope.incorrect = false;
            scope.textShown = false;
            scope.guideShown = false;
        });
    },6000)

    setTimeout(function(){questionChanger(false);},6000);
    scope.cotentProgress = Math.round((100/10)*(scope.currentAnswerIndex+1));
}

function startContent(){
    scope.startContent(contentStartDate);
}
function finishContent(){
    clearInterval(scope.playWordSoundInterval);
    contentEndDate = new Date();
    contentEndTime = new Date().getTime();
    contentTimeRequired = (contentEndTime - contentStartTime)/1000;
    scope.finishContent(contentStartDate,contentEndDate,contentTimeRequired,point,true);
}

var mockData = {
    "wordList": [
        {
            "@idx": 1,
            "wordId": 37,
            "wordKorean": "빵",
            "wordCategory": "음식",
            "blockMixGrade": 11,
            "wordGuideUrl": "contentsResources/guide/guide37.png",
            "wordImageUrl": "contentsResources/word/word37.png"
        },
        {
            "@idx": 2,
            "wordId": 15,
            "wordKorean": "소",
            "wordCategory": "동물",
            "blockMixGrade": 2,
            "wordGuideUrl": "contentsResources/guide/guide15.png",
            "wordImageUrl": "contentsResources/word/word15.png"
        },
        {
            "@idx": 3,
            "wordId": 175,
            "wordKorean": "옷걸이",
            "wordCategory": "사물",
            "blockMixGrade": 13,
            "wordGuideUrl": "contentsResources/guide/guide175.png",
            "wordImageUrl": "contentsResources/word/word175.png"
        },
        {
            "@idx": 4,
            "wordId": 102,
            "wordKorean": "편의점",
            "wordCategory": "장소",
            "blockMixGrade": 17,
            "wordGuideUrl": "contentsResources/guide/guide102.png",
            "wordImageUrl": "contentsResources/word/word102.png"
        },
        {
            "@idx": 5,
            "wordId": 210,
            "wordKorean": "이",
            "wordCategory": "신체",
            "blockMixGrade": 3,
            "wordGuideUrl": "contentsResources/guide/guide210.png",
            "wordImageUrl": "contentsResources/word/word210.png"
        },
        {
            "@idx": 6,
            "wordId": 87,
            "wordKorean": "토마토",
            "wordCategory": "채소",
            "blockMixGrade": 11,
            "wordGuideUrl": "contentsResources/guide/guide87.png",
            "wordImageUrl": "contentsResources/word/word87.png"
        },
        {
            "@idx": 7,
            "wordId": 84,
            "wordKorean": "배추",
            "wordCategory": "채소",
            "blockMixGrade": 10,
            "wordGuideUrl": "contentsResources/guide/guide84.png",
            "wordImageUrl": "contentsResources/word/word84.png"
        },
        {
            "@idx": 8,
            "wordId": 39,
            "wordKorean": "케이크",
            "wordCategory": "음식",
            "blockMixGrade": 13,
            "wordGuideUrl": "contentsResources/guide/guide39.png",
            "wordImageUrl": "contentsResources/word/word39.png"
        },
        {
            "@idx": 9,
            "wordId": 70,
            "wordKorean": "선생님",
            "wordCategory": "직업",
            "blockMixGrade": 13,
            "wordGuideUrl": "contentsResources/guide/guide70.png",
            "wordImageUrl": "contentsResources/word/word70.png"
        },
        {
            "@idx": 10,
            "wordId": 28,
            "wordKorean": "호랑이",
            "wordCategory": "동물",
            "blockMixGrade": 14,
            "wordGuideUrl": "contentsResources/guide/guide28.png",
            "wordImageUrl": "contentsResources/word/word28.png"
        }
    ],
    "isTest": [
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false
    ],
    "guideShown": [
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        true,
        true,
        true
    ]
};


(function(){
    loadResources();

}());
