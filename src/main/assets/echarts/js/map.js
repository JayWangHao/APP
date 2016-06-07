function on(data) {
	// 路径配置
	require.config({
		paths: {
			echarts: './echarts-2.2.7/build/dist'
		}
	});

	// 使用
	require(
		[
			'echarts',
			'echarts/chart/map' // 使用地图就加载map模块，按需加载
		],
		function(ec) {
			// 基于准备好的dom，初始化echarts图表
			var myChart = ec.init(document.getElementById('main'));

			var labelTop = {
				normal: {
					label: {
						show: true,
						position: 'center',
						formatter: '{b}',
						textStyle: {
							baseline: 'bottom'
						}
					},
					labelLine: {
						show: false
					}
				}
			};
			var labelFromatter = {
				normal: {
					label: {
						formatter: function(params) {
							return 100 - params.value + '%'
						},
						textStyle: {
							baseline: 'top'
						}
					}
				},
			}
			var labelBottom = {
				normal: {
					color: '#ccc',
					label: {
						show: true,
						position: 'center'
					},
					labelLine: {
						show: false
					}
				},
				emphasis: {
					color: 'rgba(0,0,0,0)'
				}
			};
			var radius = [40, 55];
			
option = {
    
    tooltip : {
        trigger: 'item'
    },
    
    dataRange: {
        x: 'left',
        y: 'bottom',
        splitList: [
           
            {start: 80, end: 100},
            {start: 60, end: 80},
            {start: 40, end: 60},
            {start: 20, end: 40},
            {start: 0, end: 20}
            
        ],
        color: ['#E0022B', '#FF6699', '#A3E00B','#FFFF00','#00FF00']
    },
    /*toolbox: {
        show: true,
        orient : 'vertical',
        x: 'right',
        y: 'center',
        feature : {
            mark : {show: true},
            dataView : {show: true, readOnly: false},
            restore : {show: true},
            saveAsImage : {show: true}
        }
    },*/
    /*roamController: {
        show: true,
        x: 'right',
        mapTypeControl: {
            'china': true
        }
    },*/
    
  "series" : [
    {
      "itemStyle" : {
        "emphasis" : {
          "borderColor" : "#fff",
          "label" : {
            "show" : true,
            "formatter" : function (params,ticket,callback) {return ticket;}
          },
          "textStyle" : {
            "color" : "#fff"
          },
          "borderWidth" : 2,
          "color" : "#32cd32"
        },
        "normal" : {
          "borderColor" : "#87cefa",
          "borderWidth" : "0.5",
          "label" : {
            "show" : true,
            "formatter" : function (params,ticket,callback) {return ticket;}
          }
        }
      },
      "mapLocation" : {
        "x" : 0,
        "y" : 0
      },
      "data" : data,
      "mapType" : "重庆",
      "nameMap" : {
        "万盛区" : "23111",
        "垫江县" : "23202",
        "大渡口区" : "224",
        "大足县" : "23100",
        "合川区" : "23101",
        "开县" : "23403",
        "江津区" : "23108",
        "渝中区" : "230",
        "忠县" : "23406",
        "永川区" : "23102",
        "梁平县" : "23404",
        "綦江县" : "23104",
        "长寿区" : "23106",
        "北新区" : "222",
        "荣昌县" : "23112",
        "北碚区" : "223",
        "巫溪县" : "23409",
        "武隆县" : "23204",
        "秀山土家族苗族自治县" : "23304",
        "江北区" : "225",
        "沙坪坝区" : "228",
        "奉节县" : "23402",
        "巫山县" : "23408",
        "九龙坡区" : "226",
        "酉阳土家族苗族自治县" : "23305",
        "南岸区" : "227",
        "铜梁县" : "23105",
        "涪陵区" : "23201",
        "渝北区" : "229",
        "城口县" : "23407",
        "璧山县" : "23107",
        "云阳县" : "23405",
        "丰都县" : "23203",
        "南川区" : "23205",
        "黔江区" : "23301",
        "石柱土家族自治县" : "23303",
        "潼南县" : "23109",
        "巴南区" : "221",
        "万州区" : "23401",
        "彭水苗族土家族自治县" : "23302"
      },
      "type" : "map",
      "roam" : false
    }
  ],
  "renderAsImage" : false,
  "animation" : false,
  "title" : {
    "padding" : 5,
    "borderColor" : "rgba(12,12,12,1.00)",
    "textStyle" : {
      "fontFamily" : "Arial, Verdana, sans-serif",
      "fontSize" : 18,
      "fontStyle" : "normal",
      "fontWeight" : "bolder",
      "decoration" : "none",
      "color" : "rgba(3,3,3,1.00)"
    },
    "subtextStyle" : {
      "fontFamily" : "Arial, Verdana, sans-serif",
      "fontSize" : 12,
      "fontStyle" : "normal",
      "fontWeight" : "normal",
      "decoration" : "none",
      "color" : "rgba(10,10,10,1.00)"
    },
    "sublink" : "",
    "x" : "left",
    "backgroundColor" : "rgba(0,0,0,0.00)",
    "y" : "top",
    "link" : "",
    "itemGap" : 5,
    "show" : true,
    "z" : 0,
    "borderWidth" : 0,
    "text" : " ",
    "subtext" : "",
    "zlevel" : 0
  },
  "calculable" : false,
  "dataRange" : {
    "x" : 0,
    "hoverLink" : false,
    "min" : 0,
    "y" : 5,
    "realtime" : false,
    "show" : true,
    "calculable" : false,
    "itemHeight" : 3,
    "color" : [
      "#FF0033",
      "#FF6699",
      "#E47833",
      " #FFFF00",
      "#00FF00"
    ],
    "max" : 100,
    "textStyle" : {
      "fontFamily" : "Arial, Verdana, sans-serif",
      "fontStyle" : "normal",
      "fontWeight" : "normal",
      "decoration" : "none",
      "fontSize" : 8
    }
  },
  "tooltip" : {
    "transitionDuration" : 0.4,
    "borderColor" : "rgba(3,3,3,1.00)",
    "padding" : 5,
    "axisPointer" : {
      "shadowStyle" : {
        "color" : "rgba(150,150,150,0.30)",
        "type" : "default"
      },
      "show" : false,
      "type" : "line",
      "lineStyle" : {
        "shadowBlur" : 5,
        "shadowOffsetX" : 3,
        "shadowOffsetY" : 3,
        "type" : "solid",
        "width" : 2,
        "color" : "rgba(4,8,11,1.00)"
      },
      "crossStyle" : {
        "shadowBlur" : 5,
        "shadowOffsetX" : 3,
        "shadowOffsetY" : 3,
        "type" : "dashed",
        "width" : 1,
        "color" : "rgba(30,144,255,1.00)"
      }
    },
    "position" : [
      520,
      20
    ],
    "islandFormmater" : "{a} < br\/>{b} : {c}",
    "backgroundColor" : "rgba(0,0,0,0.70)",
    "trigger" : "item",
    "show" : true,
    "z" : 8,
    "showContent" : true,
    "showDelay" : 20,
    "formatter" : function (params,ticket,callback) {for (var p in option.series[0].nameMap){if(option.series[0].nameMap[p] ==  params.data.name){break;}} var res = '地势:' + p + '<br/>工单占比 : ' + params.data.value+'%<br/>工单数量 : ' + params.data.value2;return res;},
    "enterable" : false,
    "hideDelay" : 100,
    "zlevel" : 1,
    "borderWidth" : 0,
    "borderRadius" : 4
  }

};
			// 为echarts对象加载数据 
			myChart.setOption(option);
		}
	);
}
