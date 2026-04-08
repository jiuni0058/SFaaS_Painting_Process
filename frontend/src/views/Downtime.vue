<template>
  <div class="page-container">
    <Sidebar :menuItems="menus" @menu-clicked="onMenuClick" />

    <div class="main-content">
      <h2 class="view-title">{{ selectedMachineName || '전체 설비' }} 다운타임 대시보드</h2>

      <div class="kpi-container">
        <div class="kpi-card">
          <h4>금일 총 다운타임</h4>
          <p>{{ kpiData.totalDowntime }} 분</p>
        </div>
        <div class="kpi-card">
          <h4>가장 빈번한 에러</h4>
          <p>{{ kpiData.mostFrequentError }}</p>
        </div>
        <div class="kpi-card">
          <h4>최장 다운타임 설비</h4>
          <p>{{ kpiData.longestDowntimeMachine }}</p>
        </div>
      </div>

      <div class="chart-container-full">
        <h3>주간 다운타임 발생 추이 (분)</h3>
        <BarChart :chartData="trendChartData" v-if="!loading" />
      </div>
      
      <div class="details-grid">
        <div class="log-container">
          <h3>상세 다운타임 로그</h3>
          <div class="log-table-wrapper">
            <table>
              <thead>
                <tr>
                  <th v-if="selectedMachineId === 'all'">설비명</th>
                  <th>시작 시간</th>
                  <th>종료 시간</th>
                  <th>소요 시간(분)</th>
                  <th>에러</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="log in downtimeLogs" :key="log.id">
                  <td v-if="selectedMachineId === 'all'">{{ log.machineName }}</td>
                  <td>{{ formatDateTime(log.startTime) }}</td>
                  <td>{{ formatDateTime(log.endTime) }}</td>
                  <td>{{ log.duration }}</td>
                  <td><span class="error-code">{{ log.errorName }}</span></td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div class="chart-container-half">
           <h3>설비별 에러 분포</h3>
           <DoughnutChart :chartData="machineErrorData" />
        </div>
        <div class="chart-container-half">
            <h3>기종별 에러 분포</h3>
            <DoughnutChart :chartData="machineTypeErrorData" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Sidebar from '@/components/SideBar.vue';
import DoughnutChart from '@/components/DoughnutChart.vue';
import BarChart from '@/components/BarChart.vue';
import axios from 'axios';

// API 엔드포인트 상수화
const DOWNTIME_API = '/api/downtime';
const DETAIL_API = '/api/detail';

export default {
  name: 'Downtime',
  components: { 
    Sidebar, 
    DoughnutChart,
    BarChart 
  },
  data() {
    return {
      menus: [{ label: '전체', icon: '🌍', machine_id: 'all' }],
      selectedMachineId: 'all',
      selectedMachineName: '',
      loading: true,
      refreshTimer: null,
      
      kpiData: { totalDowntime: 0, mostFrequentError: 'N/A', longestDowntimeMachine: 'N/A' },
      trendChartData: {
        labels: [],
        datasets: [{
          label: '다운타임(분)',
          data: [],
          backgroundColor: 'rgba(255, 99, 132, 0.5)',
          borderColor: 'rgba(255, 99, 132, 1)',
          borderWidth: 1
        }]
      },
      downtimeLogs: [],
      machineErrorData: { labels: [], datasets: [{ data: [], backgroundColor: ['#FF6B6B', '#FFD166', '#64CCA2', '#4ECDC4', '#556270'] }] },
      machineTypeErrorData: { labels: [], datasets: [{ data: [], backgroundColor: ['#C7F464', '#FF6B6B', '#4ECDC4', '#556270', '#FFD166'] }] },
    }
  },
  methods: {
    async getMachineList() {
      try {
        const response = await axios.get(DETAIL_API);
        const machines = response.data;

        // 각 머신 이름으로 ID를 조회하는 비동기 작업들을 생성합니다.
        const machineMenuPromises = machines.map(async (machineName) => {
          const idResponse = await axios.get(`${DETAIL_API}/machineid?machine_id=${encodeURIComponent(machineName)}`);
          const machineId = idResponse.data;
          return {
            label: machineName,
            icon: '🛠️',
            machine_id: machineId
          };
        });

        // 모든 ID 조회가 완료될 때까지 기다립니다.
        const machineMenus = await Promise.all(machineMenuPromises);

        this.menus = [{ label: '전체', icon: '🌍', machine_id: 'all' }, ...machineMenus];

      } catch (error) {
        console.error('Error fetching machine list:', error);
      }
    },
    
    async onMenuClick(item) {
      this.selectedMachineId = item.machine_id; // 클릭된 메뉴의 machine_id를 저장
      this.selectedMachineName = item.machine_id === 'all' ? '' : item.label;
      await this.fetchDashboardData(this.selectedMachineId); // 저장된 ID로 데이터 요청
    },

    async fetchDashboardData(machineId) {
      this.loading = true;
      try {
        const [kpiRes, trendRes, logsRes, machineErrorRes, typeErrorRes] = await Promise.all([
          axios.get(`${DOWNTIME_API}/kpi?machine_id=${machineId}`),
          axios.get(`${DOWNTIME_API}/trends?machine_id=${machineId}`),
          axios.get(`${DOWNTIME_API}/logs?machine_id=${machineId}`),
          axios.get(`${DOWNTIME_API}/errors/machine`),
          axios.get(`${DOWNTIME_API}/errors/type`),
        ]);

        this.kpiData = kpiRes.data;

        const trendData = trendRes.data;
        this.trendChartData = {
          ...this.trendChartData,
          labels: trendData.labels,
          datasets: [{ ...this.trendChartData.datasets[0], data: trendData.data }]
        };

        this.downtimeLogs = logsRes.data;

        const machineError = machineErrorRes.data;
        this.machineErrorData = {
            labels: Object.keys(machineError),
            datasets: [{ ...this.machineErrorData.datasets[0], data: Object.values(machineError) }]
        };

        const typeError = typeErrorRes.data;
        this.machineTypeErrorData = {
            labels: Object.keys(typeError),
            datasets: [{ ...this.machineTypeErrorData.datasets[0], data: Object.values(typeError) }]
        };
      } catch (error) {
        console.error('Failed to fetch dashboard data:', error);
      } finally {
        this.loading = false;
      }
    },

    formatDateTime(isoString) {
      if (!isoString) return '진행중';
      const date = new Date(isoString);
      return date.toLocaleString('ko-KR', { timeZone: 'Asia/Seoul' });
    }
  },
  async mounted() {
    await this.getMachineList();
    await this.fetchDashboardData('all');
    
    // 1분마다 데이터 자동 새로고침
    this.refreshTimer = setInterval(() => {
      this.fetchDashboardData(this.selectedMachineId);
    }, 60000);
  },
  beforeUnmount() {
    clearInterval(this.refreshTimer); // 컴포넌트 파괴 시 타이머 정리
  }
}
</script>

<style scoped>
/* 기존 스타일 유지 */
.page-container {
  display: flex;
  height: 100%;
}
.main-content {
  flex-grow: 1;
  padding: 20px;
  margin-left: 200px;
  overflow-y: auto;
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
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
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
  font-size: 2rem;
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
  grid-template-rows: auto;
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
  /* height: 350px; */ /* 삭제 */
}
h3 {
  font-size: 1.2rem;
  font-weight: 500;
  margin-bottom: 15px;
  text-align: center;
  width: 100%;
}
</style>