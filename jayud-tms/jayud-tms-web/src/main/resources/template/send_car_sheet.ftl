<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8"></meta>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"></meta>
    <meta name="viewport" content="width=device-width,initial-scale=1.0"></meta>
    <title>运单</title>
    <style type="text/css">
        body {
            font-family:SimHei;
            background: #fff;
            -webkit-text-size-adjust: 100%;
            position: absolute;
            top: 0px;
            left: 0px;
            right: 0px;
            bottom: 0px;
        }

        .neiron {
            width: 80%;
            height: auto;
            margin: 0 auto;
            border: 1px solid #0303035c;
            margin-bottom: 50px;
        }

        .gongsi {
            width: 100%;
            text-align: center;
            font-size: 22px;
            font-weight: bold;
        }

        .gongsisn {
            width: 100%;
            text-align: center;
            font-size: 16px;
            font-weight: 600;
        }

        .lianxi {
            width: 100%;
            text-align: center;
            font-size: 16px;
            font-weight: 600;
        }

        .lianxi span {
            margin-right: 10px;
        }

        .tuoyun {
            width: 100%;
            text-align: center;
            font-size: 30px;
            font-weight: bold;
            border-bottom: 1px solid #0303035c;
        }

        .gongzuo {
            width: 100%;
            border-bottom: 1px solid #0303035c;
            height: 30px;
            display: flex;

        }

        .gongzuo div {
            width: 50%;
            line-height: 30px;
        }

        .tit {
            font-size: 20px;
            font-weight: bold;

            border-right: 1px solid #0303035c;
            margin-right: 20px;
            display: inline-block;
            width: 130px;
            text-indent: 10px;
        }

        .val {
            font-size: 18px;
            font-weight: bold;
        }

        .jiange {
            width: 100%;
            height: 30px;
            line-height: 30px;
            text-indent: 10px;
            background-color: cornflowerblue;
            font-size: 16px;
        }

        .tihuo {
            width: 100%;
            display: flex;
        }

        .xinxitit {
            width: 130px;
        }

        .xinxitit div {
            width: 130px;
            line-height: 30px;
            border-bottom: 1px solid #0303035c;
            font-size: 20px;
            font-weight: bold;
            text-indent: 10px;
        }

        .dizhi {
            width: calc(100% - 130px);
            height: 123px;
            border-bottom: 1px solid #0303035c;
            border-left: 1px solid #0303035c;
            font-size: 20px;
            font-weight: bold;
            display: flex;
            /*实现垂直居中*/
            align-items: center;
            justify-content: center;
            padding: 0px 20px;
        }

        .tihuotit {
            width: 130px;
            line-height: 30px;
            border-bottom: 1px solid #0303035c;
            font-size: 20px;
            font-weight: bold;
            text-indent: 10px;
        }

        .tihuoinfo {
            width: calc(100% - 130px);
            line-height: 30px;
            border-bottom: 1px solid #0303035c;
            border-left: 1px solid #0303035c;
            color: red;
            font-size: 20px;
            font-weight: bold;
            text-align: center;
        }

        .borderleft {
            border-left: 1px solid #0303035c;
        }

        .huowutit {
            width: 100%;
            border-bottom: 1px solid #0303035c;
            height: 40px;
            display: flex;
        }

        .huowutit div {
            border-right: 1px solid #0303035c;
            font-size: 20px;
            font-weight: bold;
            width: 15%;
            text-align: center;
            line-height: 40px;
        }
        .zhuyi{
            width: 100%;
           line-height: 30px;
           font-size: 18px;
           font-weight: bold;
           text-indent: 10px;
           color: red;
        }
        .jiaojie{
            width: 100%;
            display: flex;
           
        }
        .sifen{
            width: 25%;
            height: 163px;
            border-right: 1px solid #0303035c;
        }
        .titname{
           height: 30px;
           text-align: center;
           line-height: 30px;
           font-size: 18px;
           font-weight: bold;
           border-bottom: 1px solid #0303035c;
        }
        .qianmin{
            height: 100px;
        }
        .nian{
            height: 30px;
           text-align: center;
           line-height: 30px;
           font-size: 18px;
           font-weight: bold;
            border-top: 1px solid #0303035c;
        }
        .nian span{
            margin-left: 50px;
        }
    </style>
    <script src="https://cdn.staticfile.org/angular.js/1.4.6/angular.min.js"></script>
</head>

<body>
    <div class="neiron" ng-app="myApp" ng-controller="myCtrl">
        <div class="gongsi">${obj.legalName}</div>
        <div class="gongsisn">${obj.enLegalName}</div>
        <div class="lianxi"><span>${obj.tel}</span><span>${obj.fax}</span></div>
        <div class="tuoyun">托运单</div>
        <div class="gongzuo">
            <div><span class="tit">工作单号：</span><span style="color: red;" class="val">${obj.orderNo}</span></div>
            <div><span class="tit borderleft">委托日期：</span><span class="val">${obj.jiedanTimeStr}</span></div>
        </div>
        <div class="jiange">提货信息</div>
        <div class="huowutit">
            <div>联系人</div>
            <div>联系电话</div>
            <div>提货日期</div>
            <div style="width: 200px;">车型/柜号</div>
            <div style="border: 0px;">装货地址</div>
        </div>
        <div class="huowutit" ng-repeat="info in obj.takeInfo1">
            <!--   待循环 --> 
            <div><span>{{info.contacts}}</span></div>
            <div><span>{{info.phone}}</span></div>
            <div ><span>{{info.takeTimeStr}}</span></div>
            <div style="width: 200px;"><span>{{info.vehicleType}}{{info.vehicleSize}}</span></div>
           <div style="border: 0px;"><span>{{info.stateName}}{{info.cityName}}{{info.address}}</span></div>
        </div>
        <div class="jiange"></div>
        <div class="tihuo">
            <div class="tihuotit" style="line-height: 120px">
                送货地址：
            </div>
            <div class="dizhi">
                {{obj.deliveryAddress}}
            </div>
        </div>
        <div class="tihuo">
            <div class="tihuotit" style="line-height: 120px;">
                装车要求：
            </div>
            <div class="dizhi">
                {{obj.remarks}}
            </div>
        </div>
        <div class="jiange">车辆信息</div>
        <div class="gongzuo">
            <div><span class="tit">运输车辆：</span><span class="val">{{obj.licensePlate}}/{{obj.hkLicensePlate}}</span></div>
            <div><span class="tit borderleft">司机电话：</span><span class="val">{{obj.driverPhone}}</span></div>
        </div>
        <div class="gongzuo">
            <div><span class="tit">车型/柜型：</span><span style="color: red;" class="val">{{obj.vehicleType}}/{{obj.vehicleSize}}</span></div>
            <div><span class="tit borderleft">无缝申报：</span><span class="val">无</span></div>
        </div>
        <div class="gongzuo">
            <div><span class="tit">报关口岸：</span><span class="val">{{obj.portName}}</span></div>
            <div><span class="tit borderleft">进口/出口：</span><span class="val">{{obj.goodsType}}</span></div>
        </div>
        <div class="gongzuo">
            <div><span class="tit">报关形式：</span><span class="val"></span></div>
            <div><span class="tit borderleft">报关员：</span><span class="val">{{obj.jiedanUser}}</span></div>
        </div>
        <div class="gongzuo">
            <div><span class="tit">装货人：</span><span class="val">仓库</span></div>
            <div><span class="tit borderleft">卸货人：</span><span class="val">仓库</span></div>
        </div>
        <div class="gongzuo">
            <div style="width: 100%;"><span class="tit">行车路线：</span><span class="val"></span></div>
        </div>
        <div class="tihuo">
            <div class="tihuotit" style="  display: flex; align-items: center;text-align: center;">
                香港清关地址：
            </div>
            <div class="dizhi">
                {{obj.clearCustomsAddress}}
            </div>
        </div>
        <div class="jiange">货物信息</div>
        <div class="huowutit">
            <div style="width: 130px;">NO.</div>
            <div>货物名称</div>          
            <div>件数</div>
            <div style="width: 200px;">立方数(CBM)</div>
            <div style="border: 0px;">总重(kg)</div>
        </div>
        <div class="huowutit" ng-repeat="info in obj.goddsInfos">
            <!--   待循环 -->
            <div style="width: 130px;"><span></span></div>
            <div><span>{{info.goodsDesc}}</span></div>
            <div><span>{{info.pieceAmount}}</span></div>
            <div ><span>{{info.volume}}</span></div>
            <div style="border: 0px;"><span>{{info.weight}}</span></div>
        </div>
        <div class="gongzuo">
            <div style="width: 100%;"><span class="tit" style="font-size: 16px;">合计(Total)</span><span
                    class="val">
                    总件数：{{obj.totalPieceAmount}} ，
                    总体积：{{obj.totalVolume}} ，
                    总重量：{{obj.totalWeight}} ，
                </span></div>
        </div>
        <div class="zhuyi">
            注： 若收货发现货损、货物数量不对，请及时通知我司负责人联系
        </div>
        <div class="jiange">交接信息</div>
        <div class="jiaojie">
            <div class="sifen">
                 <div class="titname">委托人</div>
                 <div class="qianmin"><span></span></div>
                 <div class="nian"><span>年</span><span>月</span><span>日</span></div>
            </div>
            <div class="sifen">
                <div class="titname">受托人</div>
                <div class="qianmin"><span></span></div>
                <div class="nian"><span>年</span><span>月</span><span>日</span></div>
           </div>
           <div class="sifen">
            <div class="titname">发货人</div>
            <div class="qianmin"><span></span></div>
            <div class="nian"><span>年</span><span>月</span><span>日</span></div>
       </div>
       <div class="sifen" style="border: 0px;">
        <div class="titname">收货人</div>
        <div class="qianmin"><span></span></div>
        <div class="nian"><span>年</span><span>月</span><span>日</span></div>
   </div>
        </div>
    </div>
    <script>
        var app = angular.module('myApp', []);
        app.controller('myCtrl', function ($scope,$location,$http) {
            $scope.obj = {
                legalName:'深圳市佳裕达国际货运代理有限公司',

            };
            $http({
                method: "GET",
                params:{orderNo:'T236488225987'},
                url:"http://192.168.3.22:8001/jayudTms/pdf/initPdfData"
            }).success(function(data){
                $scope.obj=data.data;
            });
            console.log(window.location.search)
        });
    </script>
</body>

</html>