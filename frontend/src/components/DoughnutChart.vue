<template>
  <div style="position: relative; height: 100%; width: 100%;">
    <Doughnut :data="chartData" :options="chartOptions" />
  </div>
</template>

<script>
import { Doughnut } from 'vue-chartjs'
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  ArcElement
} from 'chart.js'
import ChartDataLabels from 'chartjs-plugin-datalabels'

ChartJS.register(Title, Tooltip, Legend, ArcElement, ChartDataLabels)

export default {
  name: 'DoughnutChart',
  components: { Doughnut },
  props: {
    chartData: Object
  },
  computed: {
    chartOptions() {
      return {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'bottom'
          },
          datalabels: {
            color: '#000',
            // 데이터를 기반으로 백분율을 동적으로 계산하여 표시
            formatter: (value, context) => {
              const datapoints = context.chart.data.datasets[0].data
              const total = datapoints.reduce((total, datapoint) => total + datapoint, 0)
              const percentage = value / total * 100
              // 5% 미만 값의 라벨은 숨겨서 가독성 확보
              if (percentage < 5) {
                return '';
              }
              return `${percentage.toFixed(1)}%`
            }
          }
        }
      }
    }
  }
}
</script>