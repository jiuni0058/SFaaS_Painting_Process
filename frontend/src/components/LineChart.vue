<template>
  <div style="position: relative; height: 100%; width: 100%;">
    <Line :data="chartData" :options="chartOptions" />
  </div>
</template>

<script>
import { Line } from 'vue-chartjs'
import {
  Chart as ChartJS,
  Title, Tooltip, Legend,
  CategoryScale, LinearScale,
  PointElement, LineElement
} from 'chart.js'
import annotationPlugin from 'chartjs-plugin-annotation'

ChartJS.register(
  Title, Tooltip, Legend,
  CategoryScale, LinearScale,
  PointElement, LineElement,
  annotationPlugin
)

export default {
  name: 'LineChart',
  components: { Line },
  props: {
    chartData: Object,
    thresholdUp: Number,
    thresholdDown: Number
  },
  computed: {
    chartOptions() {
      return {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { 
            position: 'top',
            labels: {
              filter: function(legendItem, chartData) {
                // 원래 데이터셋만 범례에 표시 (annotation 라인은 제외)
                return legendItem.datasetIndex !== undefined;
              },
              generateLabels: function(chart) {
                const original = ChartJS.defaults.plugins.legend.labels.generateLabels;
                const labels = original.call(this, chart);
                
                // 상한선과 하한선을 범례에 추가
                labels.push({
                  text: '상한선',
                  fillStyle: 'rgba(255,0,0,0.5)',
                  strokeStyle: 'rgba(255,0,0,0.5)',
                  lineWidth: 2,
                  lineDash: [6, 6],
                  pointStyle: 'line'
                });
                
                labels.push({
                  text: '하한선',
                  fillStyle: 'rgba(0,0,255,0.5)',
                  strokeStyle: 'rgba(0,0,255,0.5)',
                  lineWidth: 2,
                  lineDash: [6, 6],
                  pointStyle: 'line'
                });
                
                return labels;
              }
            }
          },
          title: { display: true, text: '주간 온도 그래프' },
          annotation: {
            annotations: {
              thresholdUpLine: {
                type: 'line',
                yMin: this.thresholdUp,
                yMax: this.thresholdUp,
                borderColor: 'rgba(255,0,0,0.5)', // 빨간색 반투명
                borderWidth: 2,
                yAdjust: -10,
                borderDash: [6, 6], // 점선
                label: {
                  display: true,
                  position: 'end'
                }
              },
              thresholdDownLine: {
                type: 'line',
                yMin: this.thresholdDown,
                yMax: this.thresholdDown,
                borderColor: 'rgba(0,0,255,0.5)', // 파란색 반투명
                borderWidth: 2,
                borderDash: [6, 6], // 점선
                label: {
                  display: true,
                  position: 'end'
                }
              }
            }
          }
        },
        scales: {
          x: {
            title: {
              display: true,
              text: '시간 (HH:MM)',
              color: '#333',
              font: {
                size: 14,
                weight: 'bold'
              }
            }
          },
          y: {
            title: {
              display: true,
              text: '온도 (℃)',

              color: '#333',
              font: {
                size: 14,
                weight: 'bold'
              }
            }
          }
        }
      }
    }
  }
}
</script>