<template>
  <div class="main-content">
    <h2 class="view-title">불량 데이터 대시보드</h2>

    <div class="kpi-container">
      <div class="kpi-card">
        <h4>금일 총 검수량</h4>
        <p>{{ kpiData.totalInspections }}</p>
      </div>
      <div class="kpi-card">
        <h4>금일 불량 건수</h4>
        <p>{{ kpiData.totalDefects }}</p>
      </div>
      <div class="kpi-card">
        <h4>최근 7일 불량률</h4>
        <p>{{ kpiData.defectRate }} %</p>
      </div>
      <div class="kpi-card">
        <h4>최다 발생 불량</h4>
        <p>{{ kpiData.mostFrequentDefect }}</p>
      </div>
    </div>

    <div class="chart-container-full">
      <h3>일별 불량 발생 추이</h3>
      <BarChart :chartData="dailyTrendData" v-if="!loading" />
    </div>
    
    <div class="details-grid">
      <div class="log-container">
        <h3>상세 불량 내역 로그</h3>
        <div class="log-table-wrapper">
          <table>
            <thead>
              <tr>
                <th>발생 설비</th>
                <th>검수 시간</th>
                <th>차대번호</th>
                <th>색상</th>
                <th>불량 유형</th>
                <th>검수 담당자</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="log in defectLogs" :key="log.id">
                <td>{{ log.machineName }}</td>
                <td>{{ formatDateTime(log.inspectionTime) }}</td>
                <td>{{ log.vin }}</td>
                <td>{{ log.color }}</td>
                <td><span class="error-code">{{ log.errorName }}</span></td>
                <td>{{ log.inspectorName }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="chart-container-half">
         <h3>불량 유형별 분포</h3>
         <DoughnutChart :chartData="defectTypeData" />
      </div>
      <div class="chart-container-half">
          <h3>설비별 불량 발생 건수</h3>
          <DoughnutChart :chartData="defectByMachineData" />
      </div>
    </div>
  </div>
</template>

<script>
import DoughnutChart from '@/components/DoughnutChart.vue';
import BarChart from '@/components/BarChart.vue';
import axios from 'axios';

const API_BASE_URL = '/api/defect';

export default {
  name: 'Defect',
  components: { 
    DoughnutChart,
    BarChart 
  },
  data() {
    return {
      loading: true,
      kpiData: { totalInspections: 0, totalDefects: 0, defectRate: 0.0, mostFrequentDefect: 'N/A' },
      dailyTrendData: {
        labels: [],
        datasets: [{ label: '불량 건수', data: [], backgroundColor: 'rgba(255, 99, 132, 0.5)' }]
      },
      defectTypeData: { 
        labels: [], 
        datasets: [{ data: [], backgroundColor: ['#FF6B6B', '#FFD166', '#64CCA2', '#4ECDC4', '#556270'] }] 
      },
      defectByMachineData: { 
        labels: [], 
        datasets: [{ data: [], backgroundColor: ['#C7F464', '#FF6B6B', '#4ECDC4', '#556270', '#FFD166'] }] 
      },
      defectLogs: [],
    }
  },
  methods: {
    async fetchDashboardData() {
      this.loading = true;
      try {
        const [kpiRes, trendsRes, typeRes, machineRes, logsRes] = await Promise.all([
          axios.get(`${API_BASE_URL}/kpi`),
          axios.get(`${API_BASE_URL}/trends`),
          axios.get(`${API_BASE_URL}/errors/type`),
          axios.get(`${API_BASE_URL}/errors/machine`),
          axios.get(`${API_BASE_URL}/logs`)
        ]);

        this.kpiData = kpiRes.data;
        
        const trendData = trendsRes.data;
        this.dailyTrendData = {
          labels: trendData.labels,
          datasets: [{ ...this.dailyTrendData.datasets[0], data: trendData.data }]
        };

        const typeData = typeRes.data;
        this.defectTypeData = {
            labels: Object.keys(typeData),
            datasets: [{ ...this.defectTypeData.datasets[0], data: Object.values(typeData) }]
        };

        const machineData = machineRes.data;
        this.defectByMachineData = {
            labels: Object.keys(machineData),
            datasets: [{ ...this.defectByMachineData.datasets[0], data: Object.values(machineData) }]
        };

        this.defectLogs = logsRes.data;

      } catch (error) {
        console.error('대시보드 데이터를 가져오는 데 실패했습니다:', error);
      } finally {
        this.loading = false;
      }
    },

    formatDateTime(isoString) {
      if (!isoString) return '-';
      const date = new Date(isoString);
      return date.toLocaleString('ko-KR', { 
          year: 'numeric', month: '2-digit', day: '2-digit', 
          hour: '2-digit', minute: '2-digit', second: '2-digit',
          hour12: false 
      });
    }
  },
  mounted() {
    this.fetchDashboardData();
  }
}
</script>

<style scoped>
/* 전체 페이지 스타일 */
.main-content {
  padding: 20px;
  background-color: #f4f7f6;
}
.view-title {
  font-size: 1.8rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 20px;
}
.kpi-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}
.kpi-card {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  text-align: center;
}
.kpi-card h4 {
  font-size: 1rem;
  color: #666;
  margin-bottom: 10px;
}
.kpi-card p {
  font-size: 1.8rem;
  font-weight: 700;
  color: #667eea;
}
.chart-container-full {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  margin-bottom: 20px;
  /* height: 300px; */ /* 삭제 */
}
.details-grid {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr;
  gap: 20px;
}
.log-container {
  grid-column: 1 / 2;
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}
.log-table-wrapper {
  max-height: 400px;
  overflow-y: auto;
}
table {
  width: 100%;
  border-collapse: collapse;
}
th, td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #eee;
}
th {
  background-color: #fafafa;
  font-weight: 600;
}
.error-code {
  background-color: #FF6B6B;
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.8rem;
  font-weight: 500;
}
.chart-container-half {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  /* height: 350px;  */ /* 삭제 */
}
h3 {
  font-size: 1.2rem;
  font-weight: 500;
  margin-bottom: 15px;
  text-align: center;
  width: 100%;
}
</style>