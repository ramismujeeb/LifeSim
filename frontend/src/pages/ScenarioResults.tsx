import { useEffect, useState } from 'react';
import { useLocation, Link } from 'react-router-dom';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Legend } from 'recharts';
import api from '../api';

const ScenarioResults = () => {
  const location = useLocation();
  const [data, setData] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [monteCarlo, setMonteCarlo] = useState<any>(null);

  const scenarioId = location.state?.scenarioId;
  const name = location.state?.name || 'Scenario';

  useEffect(() => {
    if (!scenarioId) return;
    
    Promise.all([
      api.get('/simulations/baseline'),
      api.get(`/simulations/scenario/${scenarioId}/run`)
    ]).then(([baselineRes, scenarioRes]) => {
      const baselineSummaries = baselineRes.data.yearlySummaries || [];
      const scenarioSummaries = scenarioRes.data.yearlySummaries || [];
      
      const combined = baselineSummaries.map((b: any, i: number) => ({
        age: b.age,
        baseline: b.netWorth,
        scenario: scenarioSummaries[i]?.netWorth || 0
      }));
      setData(combined);
      setLoading(false);
    }).catch(err => {
      console.error(err);
      setLoading(false);
    });
  }, [scenarioId]);

  const runMonteCarlo = () => {
    if (!scenarioId) return;
    setMonteCarlo({ loading: true });
    api.get(`/simulations/scenario/${scenarioId}/monte-carlo`).then(res => {
      setMonteCarlo({ data: res.data, loading: false });
    }).catch(() => {
      setMonteCarlo({ error: 'Failed to run Monte Carlo', loading: false });
    });
  };

  if (loading) return <div className="p-8">Simulating...</div>;
  if (!scenarioId) return <div className="p-8">No scenario found.</div>;

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold text-gray-900">Simulation Results: {name}</h1>
        <Link to="/scenario/new" className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700 text-sm font-medium">
          New Scenario
        </Link>
      </div>

      <div className="bg-white shadow rounded-lg p-6">
        <h2 className="text-lg font-medium text-gray-900 mb-4">Net Worth Comparison</h2>
        <div className="h-96 w-full">
          <ResponsiveContainer width="100%" height="100%">
            <LineChart data={data} margin={{ top: 5, right: 20, bottom: 5, left: 0 }}>
              <CartesianGrid strokeDasharray="3 3" vertical={false} />
              <XAxis dataKey="age" tickLine={false} axisLine={false} />
              <YAxis tickLine={false} axisLine={false} tickFormatter={(value) => `$${value/1000}k`} />
              <Tooltip formatter={((value: any) => [`$${Number(value).toLocaleString()}`, 'Net Worth']) as any} labelFormatter={(label) => `Age: ${label}`} />
              <Legend />
              <Line type="monotone" name="Baseline" dataKey="baseline" stroke="#9ca3af" strokeWidth={2} dot={false} />
              <Line type="monotone" name="Scenario" dataKey="scenario" stroke="#4f46e5" strokeWidth={2} dot={{ r: 4 }} activeDot={{ r: 6 }} />
            </LineChart>
          </ResponsiveContainer>
        </div>
      </div>

      <div className="bg-white shadow rounded-lg p-6 mt-6">
         <h3 className="text-lg font-medium text-gray-900 mb-4">Monte Carlo Simulation</h3>
         <p className="text-sm text-gray-600 mb-4">Run 1,000 probabilistic simulations with random market variances.</p>
         
         {!monteCarlo ? (
           <button onClick={runMonteCarlo} className="bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700">Run Monte Carlo (1000 iterations)</button>
         ) : monteCarlo.loading ? (
           <p>Running simulations...</p>
         ) : monteCarlo.data ? (
           <div className="grid grid-cols-2 md:grid-cols-5 gap-4 mt-4">
              <div className="p-4 bg-gray-50 rounded">
                <p className="text-xs text-gray-500">Success Probability</p>
                <p className="text-lg font-bold">{monteCarlo.data.probabilityOfSuccess}%</p>
              </div>
              <div className="p-4 bg-gray-50 rounded">
                <p className="text-xs text-gray-500">Worst 10%</p>
                <p className="text-lg font-bold">${Number(monteCarlo.data.worst10PercentNetWorth).toLocaleString()}</p>
              </div>
              <div className="p-4 bg-gray-50 rounded">
                <p className="text-xs text-gray-500">Median</p>
                <p className="text-lg font-bold">${Number(monteCarlo.data.medianNetWorth).toLocaleString()}</p>
              </div>
              <div className="p-4 bg-gray-50 rounded">
                <p className="text-xs text-gray-500">Average</p>
                <p className="text-lg font-bold">${Number(monteCarlo.data.averageNetWorth).toLocaleString()}</p>
              </div>
              <div className="p-4 bg-gray-50 rounded">
                <p className="text-xs text-gray-500">Best 10%</p>
                <p className="text-lg font-bold">${Number(monteCarlo.data.best10PercentNetWorth).toLocaleString()}</p>
              </div>
           </div>
         ) : (
           <p className="text-red-500">{monteCarlo.error}</p>
         )}
      </div>
    </div>
  );
};

export default ScenarioResults;
