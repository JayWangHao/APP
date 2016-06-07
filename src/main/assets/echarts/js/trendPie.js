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
			'echarts/chart/pie' // 使用饼状图就加载pie模块，按需加载
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
    series : [
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
