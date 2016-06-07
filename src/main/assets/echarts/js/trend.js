function trend(){
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
			'echarts/chart/line' // 使用柱状图就加载bar模块，按需加载
		],
		function(ec) {
			// 基于准备好的dom，初始化echarts图表
			var myChart = ec.init(document.getElementById('line'));

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
   
  "legend" : {
      "orient" : "horizontal",
      "x" : "left",
      "y" : "top",
      "data" : [
        "告警量",
        "工单量"
      ],
      "show" : true,
      "z" : 4,
      "borderColor" : "#ccc",
      "itemWidth" : 20,
      "itemGap" : 10,
      "itemHeight" : 14,
      "textStyle" : {
        "fontFamily" : "Arial, Verdana, sans-serif",
        "fontSize" : 12,
        "fontStyle" : "normal",
        "fontWeight" : "normal",
        "decoration" : "none",
        "color" : "white"
      }
    },
  "renderAsImage" : false,
  "xAxis" : [
    {
      "boundaryGap" : false,
      "data" : [
        "2016-05-14",
        "2016-05-13",
        "2016-05-12",
        "2016-05-11",
        "2016-05-10",
        "2016-05-09",
        "2016-05-08",
        "2016-05-07",
        "2016-05-06",
        "2016-05-05",
        "2016-05-04",
        "2016-05-03",
        "2016-05-02",
        "2016-05-01",
        "2016-04-30",
        "2016-04-29",
        "2016-04-28",
        "2016-04-27",
        "2016-04-26",
        "2016-04-25",
        "2016-04-24",
        "2016-04-23",
        "2016-04-22",
        "2016-04-21",
        "2016-04-20",
        "2016-04-19",
        "2016-04-18",
        "2016-04-17",
        "2016-04-16",
        "2016-04-15",
        "2016-04-15"
      ],
      "type" : "category",
      "axisLabel" : {
        "rotate" : "-40",
        "interval" : 0,
        "textStyle" : {
          "color" : "white",
          "fontSize" : 10
        }
      }
    }
  ],
  "animation" : false,
  "series" : [
    {
      "name" : "告警量",
      "type" : "line",
      "data" : [
        402,
        800,
        1106,
        816,
        709,
        868,
        826,
        1976,
        1101,
        2192,
        98,
        7,
        5,
        2,
        3,
        6,
        5,
        1,
        2,
        1,
        1,
        3,
        3,
        1,
        2,
        1,
        2,
        3,
        2,
        1,
        1
      ]
    },
    {
      "name" : "工单量",
      "type" : "line",
      "data" : [
        270,
        529,
        487,
        494,
        409,
        529,
        509,
        1163,
        540,
        404,
        35,
        0,
        0,
        0,
        0,
        0,
        0,
        243,
        374,
        300,
        203,
        260,
        315,
        284,
        263,
        224,
        192,
        285,
        432,
        306,
        306
      ]
    }
  ],
  "calculable" : false,
  "grid" : {
    "x" : 35,
    "zlevel" : 0,
    "y" : 10,
    "borderWidth" : 1,
    "borderColor" : "rgba(12,12,12,1.00)",
    "z" : 0,
    "backgroundColor" : "black"
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
    "islandFormmater" : "{a} < br\/>{b} : {c}",
    "backgroundColor" : "rgba(0,0,0,0.70)",
    "trigger" : "axis",
    "show" : true,
    "z" : 8,
    "showContent" : true,
    "enterable" : false,
    "showDelay" : 20,
    "hideDelay" : 100,
    "borderWidth" : 0,
    "zlevel" : 1,
    "borderRadius" : 4
  },
  "yAxis" : [
    {
      "type" : "value",
      "axisLabel" : {
        "textStyle" : {
          "color" : "white",
          "fontSize" : 10
        }
      },
      "splitLine" : false
    }
  ]

};
			
			// 为echarts对象加载数据 
			myChart.setOption(option);
		}
	);
	
	
	// 使用
	require(
		[
			'echarts',
			'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
		],
		function(ec) {
			// 基于准备好的dom，初始化echarts图表
			var myChart = ec.init(document.getElementById('bar'));

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
    title : {
        //text: '某地区蒸发量和降水量',
        subtext: '工单告警分布(当天)'
    },
    tooltip : {
        trigger: 'axis'
    },
    /*legend: {
        data:['蒸发量','降水量']
    },*/
    /*toolbox: {
        show : true,
        feature : {
            mark : {show: true},
            dataView : {show: true, readOnly: false},
            magicType : {show: true, type: ['line', 'bar']},
            restore : {show: true},
            saveAsImage : {show: true}
        }
    },*/
    //calculable : true,
    xAxis : [
        {
        	axisLabel : {
                show:true,
                interval: 'auto',    // {number}
                rotate: 0,           //旋转
                margin: 8,
                textStyle: {
                    color: 'white',
                    fontFamily: 'sans-serif',
                    fontSize: 10,
                    fontStyle: 'italic',
                    fontWeight: 'bold'
                }
            },
            type : 'category',
            data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月','1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
        }
    ],
    yAxis : [
        {
        	axisLabel : {
                show:true,
                interval: 'auto',    // {number}
                rotate: 0,           //旋转
                margin: 8,
                textStyle: {
                    color: 'white',
                    fontFamily: 'sans-serif',
                    fontSize: 10,
                    fontStyle: 'italic',
                    fontWeight: 'bold'
                }
            },
            type : 'value'
        }
    ],
    "series" : [
    {
      "barCategoryGap" : "20%",
      "data" : [
        "9",
        "6",
        "12",
        "3",
        "7",
        "31",
        "20",
        "22",
        "25",
        "5",
        "8",
        "15",
        "17",
        "1",
        "5",
        "7",
        "5",
        "11",
        "3",
        "3",
        "1",
        "49",
        "6",
        "2",
        "5",
        "10",
        "7",
        "7",
        "16",
        "10",
        "13",
        "21",
        "31",
        "13",
        "0",
        "13",
        "3",
        "2",
        "10",
        "6"
      ],
      "type" : "bar",
      "barWidth" : 10,
      "itemStyle" : {
        "normal" : {
          "label" : {
            "show" : true,
            "position" : "insideTop"
          },
          "barBorderRadius" : 5
        }
      }
    },
    {
      "barCategoryGap" : "20%",
      "data" : [
        5,
        6,
        7,
        1,
        5,
        14,
        10,
        13,
        14,
        4,
        6,
        12,
        6,
        1,
        2,
        2,
        6,
        6,
        2,
        3,
        1,
        34,
        1,
        1,
        4,
        10,
        5,
        3,
        10,
        6,
        6,
        16,
        18,
        6,
        0,
        6,
        5,
        1,
        7,
        5
      ],
      "type" : "bar",
      "barWidth" : 10,
      "itemStyle" : {
        "normal" : {
          "label" : {
            "show" : true,
            "position" : "insideTop",
            "textStyle" : {
              "color" : "white"
            }
          },
          "barBorderRadius" : 5
        }
      }
    }
  ],
  "renderAsImage" : false,
  "xAxis" : [
    {
      "splitLine" : {
        "show" : false
      },
      "data" : [
        "巴南",
        "北新",
        "北碚",
        "大渡口",
        "江北",
        "九龙坡",
        "南岸",
        "沙坪坝",
        "渝北",
        "渝中",
        "大足",
        "合川",
        "永川",
        "綦江",
        "铜梁",
        "长寿",
        "璧山县",
        "江津",
        "潼南",
        "万盛",
        "荣昌",
        "涪陵",
        "垫江",
        "丰都",
        "武隆",
        "南川",
        "黔江",
        "彭水",
        "石柱",
        "秀山",
        "酉阳",
        "万州",
        "奉节",
        "开县",
        "梁平",
        "云阳",
        "忠县",
        "城口",
        "巫山",
        "巫溪"
      ],
      "type" : "category",
      "axisLabel" : {
        "interval" : 0,
        "textStyle" : {
          "fontSize" : 10,
          "color" : "white"
        }
      },
      "position" : "left"
    }
  ],
  "animation" : false,
  "title" : {
    "padding" : 5,
    "borderColor" : "rgba(12,12,12,1.00)",
    "textStyle" : {
      "fontFamily" : "Arial, Verdana, sans-serif",
      "fontSize" : 10,
      "fontStyle" : "normal",
      "fontWeight" : "normal",
      "decoration" : "none",
      "color" : "white"
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
    "text" : "工单告警分布(当天)",
    "subtext" : "",
    "zlevel" : 0
  },
  "calculable" : false,
  "grid" : {
    "x" : 30,
    "zlevel" : 0,
    "y" : 15,
    "z" : 0,
    "y2" : 30,
    "borderWidth" : 0,
    "x2" : 25,
    "borderColor" : "rgba(12,12,12,1.00)",
    "backgroundColor" : "rgba(0,0,0,0.00)"
  },
  "tooltip" : {
    "transitionDuration" : 0.4,
    "borderColor" : "rgba(3,3,3,1.00)",
    "padding" : 5,
    "axisPointer" : {
      "show" : false,
      "type" : "shadow"
    },
    "islandFormmater" : "{a} < br\/>{b} : {c}",
    "backgroundColor" : "rgba(0,0,0,0.70)",
    "trigger" : "axis",
    "show" : true,
    "z" : 8,
    "showContent" : true,
    "enterable" : false,
    "showDelay" : 20,
    "hideDelay" : 100,
    "borderWidth" : 0,
    "zlevel" : 1,
    "borderRadius" : 4
  },
  "yAxis" : [
    {
      "type" : "value",
      "splitLine" : {
        "show" : false
      },
      "axisLabel" : {
        "show" : false
      }
    }
  ]};
			
			// 为echarts对象加载数据 
			myChart.setOption(option);
		}
	);
	
	
	// 使用
	require(
		[
			'echarts',
			'echarts/chart/pie' // 使用柱状图就加载bar模块，按需加载
		],
		function(ec) {
			// 基于准备好的dom，初始化echarts图表
			var myChart = ec.init(document.getElementById('pie'));

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
   /* title : {
        text: '南丁格尔玫瑰图',
        subtext: '纯属虚构',
        x:'center'
    },
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
    },*/
   /* legend: {
        x : 'center',
        y : 'bottom',
        data:['rose1','rose2','rose3','rose4','rose5','rose6','rose7','rose8']
    },*/
   /* toolbox: {
        show : true,
        feature : {
            mark : {show: true},
            dataView : {show: true, readOnly: false},
            magicType : {
                show: true, 
                type: ['pie', 'funnel']
            },
            restore : {show: true},
            saveAsImage : {show: true}
        }
    },*/
    //calculable : true,
    series : [
       /* {
            name:'半径模式',
            type:'pie',
            radius : [20, 110],
            center : ['25%', 200],
            roseType : 'radius',
            width: '40%',       // for funnel
            max: 40,            // for funnel
            itemStyle : {
                normal : {
                    label : {
                        show : false
                    },
                    labelLine : {
                        show : false
                    }
                },
                emphasis : {
                    label : {
                        show : true
                    },
                    labelLine : {
                        show : true
                    }
                }
            },
            data:[
                {value:10, name:'rose1'},
                {value:5, name:'rose2'},
                {value:15, name:'rose3'},
                {value:25, name:'rose4'},
                {value:20, name:'rose5'},
                {value:35, name:'rose6'},
                {value:30, name:'rose7'},
                {value:40, name:'rose8'}
            ]
        },*/
        {
            name:'面积模式',
            type:'pie',
            radius : [30, 100],
            center : ['50%', 100],
            roseType : 'area',
            x: '50%',               // for funnel
            max: 40,                // for funnel
            sort : 'ascending',     // for funnel
            data:[
                {value:10, name:'万州片区'},
                {value:5, name:'涪陵片区'},
                {value:20, name:'黔江片区'},
                {value:40, name:'近郊片区'},
                {value:25, name:'主城片区'},
                
            ]
        }
    ]
};
			
			// 为echarts对象加载数据 
			myChart.setOption(option);
		}
	);
	
}
