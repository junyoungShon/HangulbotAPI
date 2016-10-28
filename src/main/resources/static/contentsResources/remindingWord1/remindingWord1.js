/**
 * Created by jyson on 2016. 10. 17..
 */

var serverIp = 'http://192.168.0.146:8888';


var questionList = [

    {
        remindCategory : "animal",
        backgroundList : ["sheep","rabbit","panda","monkey"],
        wordList :
            [
                //양풀토끼당근판다대나무원숭이바나나
                {wordId:"187" ,wordKorean:"양"},
                {wordId:"188" ,wordKorean:"풀"},
                {wordId:"189" ,wordKorean:"토끼"},
                {wordId:"190" ,wordKorean:"당근"},
                {wordId:"191" ,wordKorean:"판다"},
                {wordId:"192" ,wordKorean:"대나무"},
                {wordId:"193" ,wordKorean:"원숭이"},
                {wordId:"9" ,wordKorean:"바나나"}
            ]
    },
    {
        remindCategory : "shower",
        backgroundList : ["teeth","soap","water","shampoo"],
        wordList :
            [
                //칫솔이비누얼굴물손샴푸머리
                {wordId:"173" ,wordKorean:"칫솔"},
                {wordId:"210" ,wordKorean:"이"},
                {wordId:"211" ,wordKorean:"비누"},
                {wordId:"57" ,wordKorean:"얼굴"},
                {wordId:"212" ,wordKorean:"물"},
                {wordId:"213" ,wordKorean:"손"},
                {wordId:"214" ,wordKorean:"샴푸"},
                {wordId:"215" ,wordKorean:"머리"}
            ]
    },
    {
        remindCategory : "season",
        backgroundList : ["spring","summer","fall","winter"],
        wordList :
            [
                //봄개나리여름바다가을낙엽겨울눈
                {wordId:"204" ,wordKorean:"봄"},
                {wordId:"208" ,wordKorean:"개나리"},
                {wordId:"205" ,wordKorean:"여름"},
                {wordId:"130" ,wordKorean:"바다"},
                {wordId:"206" ,wordKorean:"가을"},
                {wordId:"209" ,wordKorean:"낙엽"},
                {wordId:"207" ,wordKorean:"겨울"},
                {wordId:"120" ,wordKorean:"눈"}
            ]
    },
    {
        remindCategory : "nature",
        backgroundList : ["morning","night","sky","ground"],
        wordList :
            [
                //낮해밤달하늘구름 땅 나무
                {wordId:"202" ,wordKorean:"낮"},
                {wordId:"125" ,wordKorean:"해"},
                {wordId:"203" ,wordKorean:"밤"},
                {wordId:"127" ,wordKorean:"달"},
                {wordId:"133" ,wordKorean:"하늘"},
                {wordId:"131" ,wordKorean:"구름"},
                {wordId:"129" ,wordKorean:"땅"},
                {wordId:"123" ,wordKorean:"나무"}
            ]
    },
    {
        remindCategory : "growth",
        backgroundList : ["child","frog","chicken","butterfly"],
        wordList :
            [
                //아이어른올챙이개구리병아리닭애벌레나비
                {wordId:"194" ,wordKorean:"아이"},
                {wordId:"195" ,wordKorean:"어른"},
                {wordId:"196" ,wordKorean:"올챙이"},
                {wordId:"197" ,wordKorean:"개구리"},
                {wordId:"198" ,wordKorean:"병아리"},
                {wordId:"199" ,wordKorean:"닭"},
                {wordId:"200" ,wordKorean:"애벌레"},
                {wordId:"201" ,wordKorean:"나"}
            ]
    },
    {
        remindCategory : "place",
        backgroundList : ["pharmacy","bookstore","postoffice","school"],
        wordList :
            [
                //약,약국,책,서점,편지,우체국,공부,학
                {wordId:"180" ,wordKorean:"약"},
                {wordId:"181" ,wordKorean:"약국"},
                {wordId:"182" ,wordKorean:"책"},
                {wordId:"183" ,wordKorean:"서점"},
                {wordId:"184" ,wordKorean:"편지"},
                {wordId:"185" ,wordKorean:"우체국"},
                {wordId:"186" ,wordKorean:"공부"},
                {wordId:"91" ,wordKorean:"학교"}
            ]
    }

];

var scope = angular.element(document.getElementById("contentDIV")).scope();

//카테고리 넘버는 0~5로 구성 (6단계)
var categoryNumber = Math.floor(Math.random() * 6);
var category = questionList[categoryNumber].remindCategory;


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


var initialImageLoading = function(){
    scope.startShownAnimate = false;
    scope.startBgUrl = serverIp+"/static/contentsResources/remindingWord1/images/remind_start.png"
    scope.congratulateUrl = serverIp+"/static/contentsResources/remindingWord1/images/effect_star.gif"
    scope.congrateStatus = false;
    setTimeout(function(){
        scope.$evalAsync(function() {
            scope.startShownAnimate = true;
        });

    },5000)
    scope.playBg();
    scope.backImg = serverIp+"/static/contentsResources/remindingWord1/images/"+
        questionList[5].remindCategory+"/remind_"+questionList[5].remindCategory+"_0_bg.png";
    console.log('펑션 트리거가 작동하는가')

    //스코프에 배경 그림 삽입
    scope.backgroundList =[
        serverIp+"/static/contentsResources/remindingWord1/images/"+category+"/"+category+"_0_bg.png",
        serverIp+"/static/contentsResources/remindingWord1/images/"+category+"/"+category+"_1_bg.png",
        serverIp+"/static/contentsResources/remindingWord1/images/"+category+"/"+category+"_2_bg.png",
        serverIp+"/static/contentsResources/remindingWord1/images/"+category+"/"+category+"_3_bg.png"
    ]
    //현재 배경 그림 인덱스
    scope.bgNum = 0;
    //현재 배경 보다 뒷 배경 인덱스
    scope.bgBgNum = 0;
    //현재 문제 인덱스
    scope.currentAnswerIndex = 0;

    //스코프에 가이드 이미지 url 리스트 로딩
    scope.guideList = [
        serverIp+"/static/contentsResources/remindingWord1/images/"+category+"/"+questionList[categoryNumber].wordList[0].wordId+"_guide.png",
        serverIp+"/static/contentsResources/remindingWord1/images/"+category+"/"+questionList[categoryNumber].wordList[1].wordId+"_guide.png",
        serverIp+"/static/contentsResources/remindingWord1/images/"+category+"/"+questionList[categoryNumber].wordList[2].wordId+"_guide.png",
        serverIp+"/static/contentsResources/remindingWord1/images/"+category+"/"+questionList[categoryNumber].wordList[3].wordId+"_guide.png",
        serverIp+"/static/contentsResources/remindingWord1/images/"+category+"/"+questionList[categoryNumber].wordList[4].wordId+"_guide.png",
        serverIp+"/static/contentsResources/remindingWord1/images/"+category+"/"+questionList[categoryNumber].wordList[5].wordId+"_guide.png",
        serverIp+"/static/contentsResources/remindingWord1/images/"+category+"/"+questionList[categoryNumber].wordList[6].wordId+"_guide.png",
        serverIp+"/static/contentsResources/remindingWord1/images/"+category+"/"+questionList[categoryNumber].wordList[7].wordId+"_guide.png",
    ];
    //스코프에 텍스트 이미지 url 리스트 로딩
    scope.textList = [
        serverIp+"/static/contentsResources/remindingWord1/images/"+category+"/"+questionList[categoryNumber].wordList[0].wordId+"_text.png",
        serverIp+"/static/contentsResources/remindingWord1/images/"+category+"/"+questionList[categoryNumber].wordList[1].wordId+"_text.png",
        serverIp+"/static/contentsResources/remindingWord1/images/"+category+"/"+questionList[categoryNumber].wordList[2].wordId+"_text.png",
        serverIp+"/static/contentsResources/remindingWord1/images/"+category+"/"+questionList[categoryNumber].wordList[3].wordId+"_text.png",
        serverIp+"/static/contentsResources/remindingWord1/images/"+category+"/"+questionList[categoryNumber].wordList[4].wordId+"_text.png",
        serverIp+"/static/contentsResources/remindingWord1/images/"+category+"/"+questionList[categoryNumber].wordList[5].wordId+"_text.png",
        serverIp+"/static/contentsResources/remindingWord1/images/"+category+"/"+questionList[categoryNumber].wordList[6].wordId+"_text.png",
        serverIp+"/static/contentsResources/remindingWord1/images/"+category+"/"+questionList[categoryNumber].wordList[7].wordId+"_text.png",
    ];
    //스코프에 사운드 url및 사운드 네임 로딩
    scope.remindingSoundList = [
        {
            "soundName" : category+"_0",
            "soundUrl" : "effect/reminding/"+category+"_0.mp3"
        },
        {
            "soundName" : category+"_1",
            "soundUrl" : "effect/reminding/"+category+"_0.mp3"
        },
        {
            "soundName" : category+"_2",
            "soundUrl" : "effect/reminding/"+category+"_1.mp3"
        },
        {
            "soundName" : category+"_3",
            "soundUrl" : "effect/reminding/"+category+"_1.mp3"
        },
        {
            "soundName" : category+"_4",
            "soundUrl" : "effect/reminding/"+category+"_2.mp3"
        },
        {
            "soundName" : category+"_5",
            "soundUrl" : "effect/reminding/"+category+"_2.mp3"
        },
        {
            "soundName" : category+"_6",
            "soundUrl" : "effect/reminding/"+category+"_3.mp3"
        },
        {
            "soundName" : category+"_7",
            "soundUrl" : "effect/reminding/"+category+"_3.mp3"
        }
    ]
    scope.textShown = false;
    scope.guideShown = false;
    scope.bgShown =false;
    setTimeout(function(){console.log("3초뒤 문제가 보이게 해본다.");questionChanger(true)},5000);

}

var loadResources = function(){
    scope.functionTrigger(initialImageLoading);
};
var questionChanger = function(isFirst){
    console.log('퀘스쳔체인져 실행전'+scope.textShown+scope.guideShown)
    if(isFirst){
        //처음에는 콘텐츠 시작 시간
        contentStartDate = new Date();
        contentStartTime = new Date().getTime();
        startContent(contentStartDate,contentStartTime);

    }else{
        scope.$evalAsync(function() {
            scope.congrateStatus =false;

        });
        scope.$evalAsync(function() {
            scope.textShown = false;
            scope.bgShown = false;
            scope.guideShown = false;
        });
        scope.currentAnswerIndex++;
        scope.bgNum = Math.floor(scope.currentAnswerIndex/2);
        if(scope.bgNum>=1){
            if(scope.currentAnswerIndex%2!=0){
                scope.bgBgNum = scope.bgNum;
            }else{
                scope.bgBgNum = scope.bgNum-1;
            }

        }
        console.log(scope.bgNum+" "+scope.bgBgNum);
        console.log("단어변경"+scope.currentAnswerIndex);
    }
    //마지막 문제면
    if(scope.currentAnswerIndex==8){
        finishContent();
    }else{
        scope.$evalAsync(function(){

            scope.bgShown = true;
        })
        scope.$evalAsync(function(){
            scope.guideShown = true;
        })
        //단어시작시간
        wordStartTime = new Date().getTime();
        wordStartDate = new Date();
    }
    scope.playWordSound(questionList[categoryNumber].wordList[scope.currentAnswerIndex].wordKorean)
    scope.playWordSoundInterval = setInterval(function(){
        scope.playWordSound(questionList[categoryNumber].wordList[scope.currentAnswerIndex].wordKorean)
    },4000);

    console.log('퀘스쳔체인져 실행후'+scope.textShown+scope.guideShown)
}
function submitAnswer(insertedAnswer){
    console.log('서버에서 답이 넘어옴 : '+insertedAnswer+"현재정답은?"+questionList[categoryNumber].wordList[scope.currentAnswerIndex].wordKorean);
    console.log('인터벌 클리어 이전'+scope.playWordSoundInterval);
    clearInterval(scope.playWordSoundInterval);
    console.log('인터벌 클리어 이후'+scope.playWordSoundInterval);
    scope.textShown = true;
    //단어 끝난시간 측정
    wordEndDate = new Date();
    wordEndTime = new Date().getTime();
    wordTimeRequired = (wordEndTime-wordStartTime)/1000;

    if(insertedAnswer=='나비'){
        insertedAnswer=questionList[categoryNumber].wordList[scope.currentAnswerIndex].wordKorean;
        console.log('답바꿔준다.'+insertedAnswer);
    }


    if(insertedAnswer==questionList[categoryNumber].wordList[scope.currentAnswerIndex].wordKorean){
        //정답 및 오답 여부를 서버로 전송하기 위한 클라이언트 메서드 실행
        //현재 정답 아이디 , 입력한 정답 , 정답 여부 , 가이드여부,끝낸 시간, 시작 시간 , 소요시간,테스트 여부
        scope.isConRight(questionList[categoryNumber].wordList[scope.currentAnswerIndex].wordId,insertedAnswer,true,true,
            wordEndDate,wordStartDate,wordTimeRequired,false);


        //클라이언트측 메서드로서 정답 사운드 출력
        scope.playFeedback('right');

         //축하gif
        scope.congrateStatus =true;

        //점수올려줌
        point = point+(100/8);


        //콘텐츠 진행률 100%로 변경해줌
        /*console.log('여기 서버인데 맞는것 왔어ddddd');
        DefaultGame.Game.prototype.next(true);*/
    }else{
        console.log('여기 서버인데 틀린답 왔어x');
        //정답 및 오답 여부를 서버로 전송하기 위한 클라이언트 메서드 실행
        //현재 정답 아이디 , 입력한 정답 , 정답 여부 , 끝낸 시간, 시작 시간 , 소요시간
        scope.isConWrong(questionList[categoryNumber].wordList[scope.currentAnswerIndex].wordId,insertedAnswer,false,true,
            wordEndDate,wordStartDate,wordTimeRequired,false);
        //클라이언트측 메서드로서 오답 사운드 출력
        scope.playFeedback('wrong');
    }
    scope.remindingSoundList

    setTimeout(function(){
        console.log('여기가실행이되야 소리가 나오는데');
        scope.$evalAsync(function() {
            scope.soundPlayOnce(scope.remindingSoundList[scope.currentAnswerIndex].soundName,scope.remindingSoundList[scope.currentAnswerIndex].soundUrl);
        });
    },3000);

    setTimeout(function(){
        scope.$evalAsync(function() {
            scope.congrateStatus =false;

        });
        scope.$evalAsync(function() {
            scope.textShown = false;
            scope.guideShown = false;
        });
    },6000)

    setTimeout(function(){questionChanger(false);},6500);
    scope.cotentProgress = Math.round((100/8)*(scope.currentAnswerIndex+1));
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

(function(){
    loadResources();

}());

