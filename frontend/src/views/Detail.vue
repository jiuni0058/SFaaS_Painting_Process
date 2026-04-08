<template>
  <div class="page-container">
    <Sidebar :menuItems="menus" @menu-clicked="onMenuClick" />
    <div class="content-wrapper">
      <div class="grid-card chart-card full-width-card">
        <h3 class="card-header">주요 데이터 A</h3>
        <LineChart :chartData="chartData" :thresholdUp="thresholdUp1" :thresholdDown="thresholdDown1" />
      </div>

      <div class="data-row">
        <div class="grid-card chart-card">
            <h3 class="card-header">주요 데이터 B</h3>
            <LineChart :chartData="chartData2" :thresholdUp="thresholdUp2" :thresholdDown="thresholdDown2"/>
        </div>

        <div class="grid-card chart-card">
            <h3 class="card-header">최근 1시간 가동률</h3>
            <DoughnutChart :chartData="doughnutData" />
        </div>

        <div class="grid-card info-card">
            <h3 class="card-header">설비 정보</h3>
            <div class="info-content">
              <div class="info-item">
                <span class="info-label">🕹️ 머신 이름</span>
                <span class="info-value">{{ machineName }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">🕒 마지막 점검일</span>
                <span class="info-value">{{ machineDate }}</span>
              </div>
              <div class="info-item status-indicator">
                <span class="info-label">🚦 현재 상태</span>
                <div class="status-light-wrapper">
                  <span :class="{'light-on': isMachineRun === 1, 'light-off': isMachineRun === 0}"></span>
                  <span>{{ isMachineRun === 1 ? '작동 중' : '정지됨' }}</span>
                </div>
              </div>
            </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import LineChart from '@/components/LineChart.vue'
  import Sidebar from '@/components/SideBar.vue'
  import DoughnutChart from '@/components/DoughnutChart.vue';
  import axios from 'axios';
  const DETAIL_SERVER = '/api/detail';

  export default {
      name: 'Detail',
      components: { LineChart, Sidebar, DoughnutChart },

      data() {
        return {
          machines: ['error'],
          machineID: '',
          menus: [
          { label: '홈', icon: '🏠', link: '#' },
          { label: '대시보드', icon: '📊', link: '#' },
          { label: '설정', icon: '⚙️', link: '#' },
          { label: '로그아웃', icon: '🚪', link: '/logout' }
          ],
          chartData: {
          labels: [],
          datasets: []
          },
          chartData2: {
            labels: [],
            datasets: []
          },
          thresholdUp1: 0,
          thresholdDown1: 0,
          thresholdUp2: 0,
          thresholdDown2: 0,
          machineName: '',
          machineDate: '',
          isMachineRun: 0, // isMachineRun으로 수정
          doughnutData: {
            labels: ['가동', '비가동'],
            datasets: [{
              data: [0, 100], // 초기값: 0% 가동, 100% 비가동
              backgroundColor: ['#64CCA2', '#FFD166']
            }]
          },
        }
      },
      
      methods: {
      async getDetailInformaition(machine_id) {
        try {
          const to = machine_id;
          console.log("선택된 머신 아이디:", to);
          
          // 여러 API 요청을 병렬로 처리하여 성능 향상
          const [infoResponse, machineInfoResponse, utilizationResponse] = await Promise.all([
            axios.get(`${DETAIL_SERVER}/information?machine_id=${to}`),
            axios.get(`${DETAIL_SERVER}/machineInformation?machine_id=${to}`),
            axios.get(`${DETAIL_SERVER}/utilization?machine_id=${to}`) // ✅ 가동률 API 호출 추가
          ]);

          // 설비 정보 업데이트
          const machineInfoData = machineInfoResponse.data;
          this.machineName = machineInfoData.machine_name + " " + to;
          this.machineDate = machineInfoData.machine_date;
          this.isMachineRun = machineInfoData.is_machine_run;

          // 📈 가동률 차트 데이터 업데이트 (새로운 방식)
          const utilizationData = utilizationResponse.data;
          const utilizationRate = utilizationData.utilizationRate;
          this.doughnutData = {
            labels: ['가동', '비가동'],
            datasets: [{
              data: [utilizationRate, 100 - utilizationRate], // API에서 받은 값으로 설정
              backgroundColor: ['#64CCA2', '#FFD166']
            }]
          };
          console.log("최근 1시간 가동률:", utilizationRate, "%");

          // 라인 차트 데이터 업데이트
          const infoData = infoResponse.data;
          const keys = Object.keys(infoData);

          if (keys.length > 0) {
            const arr1 = infoData[keys[0]].slice(-5);
            this.thresholdUp1 = arr1[0].threshold_up;
            this.thresholdDown1 = arr1[0].threshold_down;
            this.chartData = {
              labels: arr1.map(d => d.plc_create_dt.slice(11, 16)),
              datasets: [
                {
                  label: keys[0],
                  data: arr1.map(d => Number(d.value)),
                  borderColor: '#64CCA2',
                  backgroundColor: 'rgba(100,204,162,0.2)',
                  fill: true,
                  tension: 0.3,
                  datalabels: { display: true, align: 'top', offset: 1, color: '#333', font: { size: 10, weight: 'bold' } }
                }
              ]
            };
          }

          if (keys.length > 1) {
            const arr2 = infoData[keys[1]].slice(-5);
            this.thresholdUp2 = arr2[0].threshold_up;
            this.thresholdDown2 = arr2[0].threshold_down;
            this.chartData2 = {
              labels: arr2.map(d => d.plc_create_dt.slice(11, 16)),
              datasets: [
                {
                  label: keys[1],
                  data: arr2.map(d => Number(d.value)),
                  borderColor: '#FF6B6B',
                  backgroundColor: 'rgba(255,107,107,0.2)',
                  fill: true,
                  tension: 0.3,
                  datalabels: { display: true, align: 'top', offset: 1, color: '#333', font: { size: 10, weight: 'bold' } }
                }
              ]
            };
          }
        } catch (error) {
          console.error('Error during detail fetch:', error);
        }
      },
      async getMachineName(){
        try{
          const response = await axios.get(`${DETAIL_SERVER}`);
          const machines = response.data;
          console.log("받은 머신이름:", machines);
          this.machines = machines;
          const machineMenus = machines.map(m => ({
            label: m,
            icon: '🛠️',
            link: '#',
            machine_id: m.machine_id 
          }));
          this.menus = [
            ...machineMenus, 
          ];
        }catch(error){
          console.log("에러났음 ")
          console.error('Error during machine name fetch:', error);
        }
      },
      async getMachineID(item){
        try{
              const response = await axios.get(`${DETAIL_SERVER}/machineid?machine_id=${encodeURIComponent(item)}`);
              const machineId = response.data;
              console.log("받은 머신별 데이터:", machineId);
              return machineId
          }catch(error){
            console.error('Error during machine-specific fetch:', error);
        }
      },
      async onMenuClick(item) {
        console.log("클릭된 메뉴:", item.label);
        const machine_id = await this.getMachineID(item.label);
        this.machineID = machine_id;
        await this.getDetailInformaition(machine_id);
      }
    },
      mounted() {
        this.getMachineName();
        this.refreshTimer = setInterval(() => {
        if (this.machineID) {
          this.getDetailInformaition(this.machineID);
        }
          }, 60000); 
        },
        beforeUnmount() {
          clearInterval(this.refreshTimer);
        }
  }
</script>

<style scoped>
/* style 태그 내용은 변경 사항이 없으므로 그대로 유지합니다. */
/* 전체 페이지 레이아웃 */
.page-container {
  display: flex;
  height: 100%;
  background-color: var(--color-background-mute);
}

.content-wrapper {
  flex-grow: 1;
  margin-left: 200px;
  padding: 24px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* 데이터 행 레이아웃 (Flexbox 사용) */
.data-row {
  display: flex;
  gap: 24px;
  align-items: stretch; /* 카드 높이를 동일하게 맞춤 */
}

/* 공통 카드 스타일 */
.grid-card {
  background-color: var(--color-background);
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  border: 1px solid var(--color-border);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  display: flex;
  flex-direction: column;
}

.grid-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
}

.card-header {
  margin: 0 0 16px 0;
  font-size: 18px;
  font-weight: 600;
  color: var(--color-heading);
  border-bottom: 1px solid var(--color-border);
  padding-bottom: 12px;
}

/* 차트 카드 스타일 
.chart-card {
  height: 350px;
}
  */

/* 중요도에 따른 너비 조절 */
.data-row > .chart-card {
  flex: 2; /* 차트 카드들은 2의 비율을 가짐 */
}

.data-row > .info-card {
  flex: 1; /* 정보 카드는 1의 비율을 가짐 (차트의 절반 크기) */
  height: 350px;
}

/* 정보 카드 스타일 */
.info-content {
  display: flex;
  flex-direction: column;
  gap: 16px; /* 간격 살짝 줄임 */
  font-size: 15px; /* 폰트 크기 살짝 줄임 */
  flex-grow: 1;
  justify-content: center;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-label {
  color: var(--vt-c-text-light-2);
  font-weight: 500;
}

.info-value {
  color: var(--color-text);
  font-weight: 600;
  text-align: right;
}

/* 기계 상태 표시등 */
.status-indicator .status-light-wrapper {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
}

.light-on, .light-off {
  width: 14px;
  height: 14px;
  border-radius: 50%;
}

.light-on {
  background-color: #28a745;
  box-shadow: 0 0 8px #28a745;
}

.light-off {
  background-color: #6c757d;
}

/* 캔버스 크기 조정 */
.grid-card canvas {
  width: 100% !important;
  height: 100% !important;
}

.full-width-card {
  height: 500px; /* 원하는 높이로 조절 */
}
</style>
