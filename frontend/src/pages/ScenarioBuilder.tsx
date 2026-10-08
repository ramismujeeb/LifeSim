import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { PlusCircle, Trash2 } from 'lucide-react';
import api from '../api';

const eventTypes = ['JOB_LOSS', 'SALARY_GROWTH', 'ONE_TIME_EXPENSE', 'LOAN', 'INVESTMENT', 'RECURRING_EXPENSE'];

const ScenarioBuilder = () => {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [events, setEvents] = useState<{ type: string; age: number; amount: number; duration: number; interestRate: number; percentage: number }[]>([]);
  const navigate = useNavigate();

  const addEvent = () => {
    setEvents([...events, { type: 'ONE_TIME_EXPENSE', age: 35, amount: 0, duration: 0, interestRate: 0, percentage: 0 }]);
  };

  const removeEvent = (index: number) => {
    setEvents(events.filter((_, i) => i !== index));
  };

  const updateEvent = (index: number, field: string, value: string | number) => {
    const newEvents = [...events];
    newEvents[index] = { ...newEvents[index], [field]: value };
    setEvents(newEvents);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      // mapping frontend 'events' to backend 'changes' (startAge, startMonth, durationMonths, amount, percentage, interestRate)
      const changes = events.map(ev => ({
        type: ev.type,
        startAge: ev.age,
        startMonth: 1,
        durationMonths: ev.duration,
        amount: ev.amount,
        percentage: ev.percentage,
        interestRate: ev.interestRate
      }));
      const res = await api.post('/scenarios', { name, description, changes });
      navigate('/scenario/results', { state: { scenarioId: res.data.id, name } });
    } catch (err) {
      console.error(err);
      alert('Failed to save scenario');
    }
  };

  return (
    <div className="bg-white shadow rounded-lg p-6 max-w-4xl mx-auto">
      <h2 className="text-2xl font-bold text-gray-900 mb-6">Build a Scenario</h2>
      
      <form onSubmit={handleSubmit} className="space-y-6">
        <div className="grid grid-cols-1 gap-6 sm:grid-cols-2">
          <div className="sm:col-span-2">
            <label className="block text-sm font-medium text-gray-700">Scenario Name</label>
            <input type="text" required value={name} onChange={e => setName(e.target.value)} className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" />
          </div>
          <div className="sm:col-span-2">
            <label className="block text-sm font-medium text-gray-700">Description</label>
            <textarea value={description} onChange={e => setDescription(e.target.value)} rows={3} className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" />
          </div>
        </div>

        <div className="mt-8">
          <div className="flex justify-between items-center mb-4">
            <h3 className="text-lg font-medium text-gray-900">Life Events</h3>
            <button type="button" onClick={addEvent} className="inline-flex items-center px-3 py-1.5 border border-transparent text-sm font-medium rounded text-indigo-700 bg-indigo-100 hover:bg-indigo-200">
              <PlusCircle className="w-4 h-4 mr-1" /> Add Event
            </button>
          </div>
          
          <div className="space-y-4">
            {events.map((ev, idx) => (
              <div key={idx} className="flex items-center space-x-4 p-4 border border-gray-200 rounded-md bg-gray-50">
                <div className="flex-1 grid grid-cols-1 gap-4 sm:grid-cols-6">
                  <div>
                    <label className="block text-xs font-medium text-gray-700">Type</label>
                    <select value={ev.type} onChange={e => updateEvent(idx, 'type', e.target.value)} className="mt-1 block w-full border-gray-300 rounded-md py-2 px-3 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm">
                      {eventTypes.map(t => <option key={t} value={t}>{t.replace(/_/g, ' ')}</option>)}
                    </select>
                  </div>
                  <div>
                    <label className="block text-xs font-medium text-gray-700">Age</label>
                    <input type="number" value={ev.age} onChange={e => updateEvent(idx, 'age', Number(e.target.value))} className="mt-1 block w-full border border-gray-300 rounded-md py-2 px-3 sm:text-sm" />
                  </div>
                  <div>
                    <label className="block text-xs font-medium text-gray-700">Amount / Value</label>
                    <input type="number" value={ev.amount} onChange={e => updateEvent(idx, 'amount', Number(e.target.value))} className="mt-1 block w-full border border-gray-300 rounded-md py-2 px-3 sm:text-sm" />
                  </div>
                  <div>
                    <label className="block text-xs font-medium text-gray-700">Duration (Months)</label>
                    <input type="number" value={ev.duration} onChange={e => updateEvent(idx, 'duration', Number(e.target.value))} className="mt-1 block w-full border border-gray-300 rounded-md py-2 px-3 sm:text-sm" />
                  </div>
                  <div>
                    <label className="block text-xs font-medium text-gray-700">Interest (%)</label>
                    <input type="number" value={ev.interestRate} onChange={e => updateEvent(idx, 'interestRate', Number(e.target.value))} className="mt-1 block w-full border border-gray-300 rounded-md py-2 px-3 sm:text-sm" />
                  </div>
                  <div>
                    <label className="block text-xs font-medium text-gray-700">Percentage (%)</label>
                    <input type="number" value={ev.percentage} onChange={e => updateEvent(idx, 'percentage', Number(e.target.value))} className="mt-1 block w-full border border-gray-300 rounded-md py-2 px-3 sm:text-sm" />
                  </div>
                </div>
                <button type="button" onClick={() => removeEvent(idx)} className="text-red-500 hover:text-red-700 p-2">
                  <Trash2 className="w-5 h-5" />
                </button>
              </div>
            ))}
            {events.length === 0 && <p className="text-sm text-gray-500 text-center py-4">No events added. This will just run your baseline profile.</p>}
          </div>
        </div>

        <div className="flex justify-end pt-5">
          <button type="submit" className="bg-indigo-600 text-white px-6 py-2 rounded-md hover:bg-indigo-700 text-sm font-medium shadow">
            Save & Run Simulation
          </button>
        </div>
      </form>
    </div>
  );
};

export default ScenarioBuilder;
