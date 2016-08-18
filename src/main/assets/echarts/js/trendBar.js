function trend(once,alarm,city){
	require.config({
		paths: {
			echarts: './echarts-2.2.7/build/dist'
		}
	});
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
        subtext: '工单告警分布(当天)'
    },
    tooltip : {
        trigger: 'axis'
    },
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
            type : 'category',
            data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月','1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
        }
    ],
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
            type : 'value'
        }
    ],
    "series" : [
    {
      "barCategoryGap" : "20%",
      "data" : once,
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
      "data" : alarm,
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
  "yAxis" : [
    {
      "splitLine" : {
        "show" : false
      },
      "data" : city,
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
    "text" : "工单告警分布(今日top5)      红色：告警     蓝色：工单",
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
  "xAxis" : [
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
}
