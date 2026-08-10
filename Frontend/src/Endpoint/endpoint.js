const baseurl = `http://localhost:30090`

// 
// GET https://fakestoreapi.com/products
// GET https://fakestoreapi.com/users

// demo url 

const demoURL = `http://localhost:30090`

// Health Endpoint

export const Healthcheckurl = `${demoURL}/api/health`

// Alert Endpoints

export const getAllalerts = `${demoURL}/api/alerts`
export const getAlertbyID = `${demoURL}/api/alerts/{alertId}`
export const getseveritybyalert = `${demoURL}/api/alerts/severity/{severity}/CRITICAL`
export const getseveritybyalertWarning = `${demoURL}/api/alerts/severity/{severity}/WARNING`
export const getHostnamebyalert = `${demoURL}/api/alerts/hostname/{hostname}`

// Metrics Endpoints

export const getAllMetrics = `${demoURL}/api/metrics`
export const gethostnamebymetrics = `${demoURL}/api/metrics/hostname`
export const gethighcpubymetrics = `${demoURL}/api/metrics/high-cpu`
export const gethighmemorybymetrics = `${demoURL}/api/metrics/high-memory`

// Monitoring and Trap Endpoints

export const getAllTraps = `${demoURL}/api/monitoring/traps`
export const getAllrecentTraps = `${demoURL}/api/monitoring/traps/recent`
export const getAllhostTraps = `${demoURL}/monitoring/traps/host/{host}`
export const getAllseverityTraps = `${demoURL}/monitoring/traps/severity/{severity}`
export const getAllseverityTrapsWarning = `${demoURL}monitoring/traps/severity/{severity}`
export const getTrapbyID = `${demoURL}/api/monitoring/traps/{trapId}`
export const gethostnamebycollect = `${demoURL}/api/monitoring/collect/{hostname}`
export const gethostnamebyStatus = `${demoURL}/api/monitoring/status/{hostname}`
export const gethostnamebyAlert = `${demoURL}/api/monitoring/alerts/{hostname}`
export const getAllbulckreport = `${demoURL}/api/monitoring/bulk`

// Dashboard Endpoint

export const getDashboardSummary = `${demoURL}/api/dashboard/summary`


export const EventURL = `${baseurl}/api/alerts/hostname/server1`;
export const AlertURL = `${baseurl}/users`;


export const Todaydemo = `http://localhost:30190`

export const eventsUrl = `http://localhost:30096`

export const userDataUrl = `http://localhost:30097`

export const customerPlansUrl = `http://localhost:30098`







 