<template>
  <div style="position: relative; height: 100%; width: 100%;">
    <Bar :data="chartData" :options="chartOptions" />
  </div>
</template>

<script>
import { Bar } from 'vue-chartjs'
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale
} from 'chart.js'
import ChartDataLabels from 'chartjs-plugin-datalabels';

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale, ChartDataLabels);

export default {
  name: 'BarChart',
  components: { Bar },
  props: {
    chartData: {
      type: Object,
      required: true
    }
  },
  computed: {
    chartOptions() {
      return {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: false // 범례는 차트 위에 제목으로 표시되므로 숨김
          },
          datalabels: {
            anchor: 'end',
            align: 'top',
            formatter: (value) => `${value}분`,
            color: '#555',
            font: {
              weight: 'bold'
            }
          }
        },
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    }
  },
  watch: {
    chartData: {
        deep: true,
        handler(newData) {
        if (this.chartInstance) {
            // 새 데이터로 차트 업데이트
            this.chartInstance.data = newData;
            this.chartInstance.update(); // 차트 다시 그리기
        } else {
            // 차트 인스턴스가 없으면 새로 렌더링
            this.renderChart();
        }
        }
    }
    }
}
</script>