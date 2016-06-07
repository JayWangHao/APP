
function trend(ztime,gaoOnce,gongOnce){

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
			'echarts/chart/line' // 使折线图就加载line模块，按需加载
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
      "data" :ztime,
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
      "data" : gaoOnce
    },
    {
      "name" : "工单量",
      "type" : "line",
      "data" : gongOnce
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


}
