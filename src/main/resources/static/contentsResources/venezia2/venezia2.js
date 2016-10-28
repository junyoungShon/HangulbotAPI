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
scope.questionList = [];

var currentAnswerIndex=0;



var initialImageLoading = function(){
    scope.startShownAnimate = false;
    //scope.bgUrl = serverIp+"/static/contentsResources/defaultWordCard1/images/play_start.png"
    scope.startBgUrl = serverIp+"/static/contentsResources/venezia2/images/play_start.png"
    scope.bgUrl = serverIp+"/static/contentsResources/venezia2/images/bg.png"
    scope.congratulateUrl = serverIp+"/static/contentsResources/defaultWordCard1/images/effect_star.gif"
    scope.congrateStatus = false;
    getQuestionList();
    setTimeout(function(){
        scope.$evalAsync(function() {
            scope.startShownAnimate = true;
        });
    },5000)
    scope.playBg();


    setTimeout(function(){console.log("3초뒤 문제가 보이게 해본다.");questionChanger(true)},5000);

}
//출제될 단어 목록을 받아옴
function getQuestionList(){
    var data =  scope.getQuestionList();
    //var data = mockData;
    scope.imageList = [];
    scope.guideList = [];
    for(var i=0;i<2;i++){
        scope.questionList.push(data.wordList[i]);
        scope.questionList[i].wordImageUrl = serverIp+"/static/"+scope.questionList[i].wordImageUrl;
        scope.questionList[i].wordGuideUrl = serverIp+"/static/"+scope.questionList[i].wordGuideUrl;
        scope.questionList[i].isCorrect = false;
        scope.questionList[i].isIncorrect = false;
    }


    console.log('로딩된 단어',scope.questionList);
}
var loadResources = function(){
    scope.functionTrigger(initialImageLoading);
};


var movingQuestion1;
var movingQuestion2;
var question0Status = 'moving';
var question1Status = 'moving';
var questionChanger = function(isFirst){
    console.log('퀘스쳔체인져 실행전'+scope.textShown+scope.guideShown)
    if(isFirst){
        //처음에는 콘텐츠 시작 시간
        contentStartDate = new Date();
        contentStartTime = new Date().getTime();
        startContent(contentStartDate,contentStartTime);
        firstQuestionPosition('first');
        secondQuestionPosition('first');
        scope.$evalAsync(function() {
            scope.congrateStatus =false;
            movingQuestion1 = setInterval(function(){firstQuestionPosition(question0Status)},1000/30);
            movingQuestion2 = setInterval(function(){secondQuestionPosition(question1Status)},1000/30);
        });
    }else{

        
    }
    //마지막 문제면
    if(currentAnswerIndex==scope.questionList.length){
        scope.soundPlayOnce('finalSoundVenezia','effect/venezia2/veneziaFadeout.mp3');
        scope.$evalAsync(function() {
            scope.isFinal = true;

        })
        setTimeout(function(){finishContent();},4000)

    }else{

    }
}
function submitAnswer(insertedAnswer){
    console.log('서버에서 답이 넘어옴 : '+insertedAnswer+"현재정답은?"+scope.questionList[currentAnswerIndex].wordKorean);

    var flag = false;
    if(insertedAnswer=='나비'){
        insertedAnswer=scope.questionList[currentAnswerIndex].wordKorean;
        console.log('답바꿔준다.'+insertedAnswer);
    }
    for(var i=0;i<scope.questionList.length;i++){
        if(scope.questionList[i].isCorrect){
            
        }else{
            if(scope.questionList[i].wordKorean==insertedAnswer){
                scope.playFeedback('right');
                if(i==0){
                    firstQuestionCorrect();
                    flag = true;
                }else{
                    secondQuestionCorrect();
                    flag = true;
                }
                break;
            }
        }
    }
    //틀렸을 때
    if(!flag){
        scope.playFeedback('wrong');
        scope.questionList[currentAnswerIndex].isInCorrect = true;
        setTimeout(function(){
            scope.$evalAsync(function() {
                scope.questionList[0].isInCorrect = false;
                scope.questionList[1].isInCorrect = false;
            })
        },1500)
    }

}
function firstQuestionCorrect(){
    scope.question0MiddleAni = true;

    setTimeout(function(){

        console.log('맞았어!!!!!!!!!!!')
        scope.$evalAsync(function() {
            scope.questionList[0].isCorrect = true;
        })
        setTimeout(function(){
            scope.$evalAsync(function() {
                scope.soundPlayOnce('rightSoundInVenezia','effect/venezia2/veneziaFadein.mp3');
                scope.question0FinalAni = true;
            })

        },1000)


    },2000)
    currentAnswerIndex++
    setTimeout(function(){
        scope.soundPlayOnce('rightSoundInVenezia','effect/venezia2/veneziaFadein.mp3');
        questionChanger()
    },4000);
}
function secondQuestionCorrect(){
    scope.question1MiddleAni = true;
    setTimeout(function(){
        scope.$evalAsync(function() {
            console.log('맞았어!!!!!!!!!!!')
            scope.question1FinalAni = true;
            scope.questionList[1].isCorrect = true;
        })
    },2000)
    currentAnswerIndex++
    setTimeout(function(){
        questionChanger()
    },3000);
}
function startContent(){
    scope.startContent(contentStartDate);
}
function finishContent(){
    contentEndDate = new Date();
    contentEndTime = new Date().getTime();
    contentTimeRequired = (contentEndTime - contentStartTime)/1000;
    scope.finishContent(contentStartDate,contentEndDate,contentTimeRequired,point,true);
}
var canvasHeight;
var canvasWidth;
scope.question0 = {'x':-400,'y':0,'vx':0,'vy':0,'width':0,'height':0};
scope.question1 = {'x':-400,'y':0,'vx':0,'vy':0,'width':0,'height':0};
var firstIsCollided = false;
var secondIsCollided = false;
function firstQuestionPosition(status){
    if(status=='first'){
        canvasHeight = scope.deviceHeight-(scope.deviceHeight/8)-((scope.deviceWidth*22.8)/100);
        canvasWidth = scope.deviceWidth-((scope.deviceWidth*30)/100);
        scope.question0.width = (scope.deviceWidth*30)/100;
        scope.question0.height = (scope.deviceWidth*22.8)/100;
        scope.question0.x = nextRandomInteger(canvasWidth);
        scope.question0.y = nextRandomInteger(canvasHeight);
        scope.question0.vx = randomSpeed(7);
        scope.question0.vy = randomSpeed(7);

    }else if(status=='stop'){

    }else{

        // 범위 검사
        if (scope.question0.x < 0 || scope.question0.x > canvasWidth) { scope.question0.vx *= -1; }
        if (scope.question0.y < 0 || scope.question0.y > canvasHeight) { scope.question0.vy *= -1; }
        if (isCollide(scope.question0,scope.question1)&&firstIsCollided){
            scope.question0.vx *= -1
        }else{
            firstIsCollided = false;
        }
        scope.$evalAsync(function() {
            // 이동
            scope.question0.x += scope.question0.vx;
            scope.question0.y += scope.question0.vy;
        })

    }
}
function secondQuestionPosition(status){
    if(status=='first'){
        canvasHeight = scope.deviceHeight-(scope.deviceHeight/8)-((scope.deviceWidth*22.8)/100);
        canvasWidth = scope.deviceWidth-((scope.deviceWidth*30)/100);
        scope.question1.width = (scope.deviceWidth*30)/100;
        scope.question1.height = (scope.deviceWidth*22.8)/100;
        scope.question1.x = nextRandomInteger(canvasWidth);
        scope.question1.y = nextRandomInteger(canvasHeight);
        scope.question1.vx = randomSpeed(7);
        scope.question1.vy = randomSpeed(7);
    }else if(status=='stop'){

    }else{
        // 범위 검사
        if (scope.question1.x < 0 || scope.question1.x > canvasWidth) { scope.question1.vx *= -1; }
        if (scope.question1.y < 0 || scope.question1.y > canvasHeight) { scope.question1.vy *= -1; }
        if (isCollide(scope.question0,scope.question1)&&secondIsCollided){
            scope.question1.vy *= -1
        }else{
            secondIsCollided = false;
        }
        scope.$evalAsync(function() {
            // 이동
            scope.question1.x += scope.question1.vx;
            scope.question1.y += scope.question1.vy;
        })
    }
}
function isCollide(a, b) {
    console.log('isCollide실행');
    firstIsCollided = true;
    secondIsCollided = true;
    return false;/*!(

        ((a.y + a.height) < (b.y)) ||
        (a.y > (b.y + b.height)) ||
        ((a.x + a.width) < b.x) ||
        (a.x > (b.x + b.width))
    );*/
}
function nextRandomInteger(limit) {
    return Math.round(Math.random() * limit);
}
// 양음으로 랜덤한 속도를 생성하는 함수입니다.
function randomSpeed(maxSpeed) {
    var speed = Math.floor( (Math.random() * (maxSpeed - (maxSpeed-5) + 1)) + (maxSpeed-5) );
    if(Math.random()>0.5){
        speed *= -1;
    }
    return speed;
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

