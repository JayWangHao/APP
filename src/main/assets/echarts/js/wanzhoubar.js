function on(complete,total,address){
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
			'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
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
        trigger: 'axis'
    },
    /*legend: {
        data:[
            'ECharts1 - 2k数据','ECharts1 - 2w数据','ECharts1 - 20w数据','',
            'ECharts2 - 2k数据','ECharts2 - 2w数据','ECharts2 - 20w数据'
        ]
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
    calculable : true,
    
    xAxis : [
      {
            type : 'value',
            axisLabel:{formatter:'{value} ms'}
        }
        
    ],
    yAxis : [
        {
            type : 'category',
            data : ['Line','Bar','Scatter','K','Map']
        },
        {
            type : 'category',
            axisLine: {show:false},
            axisTick: {show:false},
            axisLabel: {show:false},
            splitArea: {show:false},
            splitLine: {show:false},
            data : ['Line','Bar','Scatter','K','Map']
        }
    ],
    
  "series" : [
    {
      "barCategoryGap" : "10%",
      "data" : complete,
      "stack" : "sum",
      "type" : "bar",
      "barWidth" : 10,
      "itemStyle" : {
        "normal" : {
          "label" : {
            "show" : true,
            "position" : "insideRight"
          },
          "barBorderWidth" : 0.2,
          "barBorderRadius" : 1,
          "barBorderColor" : "blue",
          "color" : function(value){i = value.dataIndex;if(value.data/(option.series[1].data[i]+value.data)>0.6 & value.data/(option.series[1].data[i]+value.data)<0.8 ){return "orange";}if(value.data/(option.series[1].data[i]+value.data)>0.8){return "red";}else{return "green";}}
        }
      }
    },
    {
      "barCategoryGap" : 20,
      "data" : total,
      "stack" : "sum",
      "type" : "bar",
      "barWidth" : 10,
      "itemStyle" : {
        "normal" : {
          "label" : {
            "show" : true,
            "textStyle" : {
              "color" : "white"
            },
            "position" : "right",
            "formatter" : function (params) {for (var i = 0, l = option.yAxis[0].data.length; i < l; i++) {if (option.yAxis[0].data[i] == params.name) {return option.series[0].data[i] + params.value;}}}
          },
          "barBorderWidth" : 0.2,
          "barBorderRadius" : 1,
          "barBorderColor" : "blue",
          "color" : "white"
        }
      }
    }
  ],
  "renderAsImage" : false,
  "xAxis" : [
    {
      "type" : "value",
      "splitLine" : {
        "show" : false
      }
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
    "text" : "工单完成率(%)   0~60 绿色  60~80橙色  80~100红色",
    "subtext" : "",
    "zlevel" : 0
  },
  "calculable" : false,
  "grid" : {
    "x" : 30,
    "zlevel" : 0,
    "y" : 10,
    "z" : 0,
    "y2" : 0,
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
      "axisLabel" : {
        "interval" : 0,
        "textStyle" : {
          "fontSize" : 10,
          "color" : "white"
        }
      },
      "splitLine" : {
        "show" : false
      },
      "position" : "left",
      "data" :address,
      "axisTick" : "false",
      "axisLine" : "false",
      "type" : "category"
    }
  ]

};
             
			
			// 为echarts对象加载数据 
			myChart.setOption(option);
		}
	);
}