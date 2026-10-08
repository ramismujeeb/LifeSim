import React, { useState, useEffect } from 'react';
import api from '../api';

const Profile = () => {
  const [profile, setProfile] = useState({
    currentAge: 30,
    retirementAge: 65,
    monthlySalary: 5000,
    currentSavings: 50000,
    monthlyRent: 1500,
    monthlyLivingExpenses: 1000,
    monthlyInvestment: 500,
    annualSalaryGrowthPercent: 3,
    annualInflationPercent: 2,
    annualInvestmentReturnPercent: 7,
    goalTargetAmount: 1000000,
    goalTargetAge: 65,
  });
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');

  useEffect(() => {
    api.get('/profile').then(res => {
      if (res.data) setProfile(res.data);
      setLoading(false);
    }).catch(() => {
      setLoading(false); // keep mock
    });
  }, []);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setProfile(prev => ({ ...prev, [name]: parseFloat(value) || 0 }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post('/profile', profile);
      setMessage('Profile updated successfully!');
    } catch {
      setMessage('Error updating profile!');
    }
  };

  if (loading) return <div>Loading...</div>;

  return (
    <div className="bg-white shadow rounded-lg p-6 max-w-3xl mx-auto">
      <h2 className="text-xl font-bold text-gray-900 mb-6">Financial Profile</h2>
      {message && <div className="mb-4 text-green-600 bg-green-50 p-3 rounded">{message}</div>}
      
      <form onSubmit={handleSubmit} className="space-y-6">
        <div className="grid grid-cols-1 gap-y-6 gap-x-4 sm:grid-cols-2">
          
          <div>
            <label className="block text-sm font-medium text-gray-700">Current Age</label>
            <input type="number" name="currentAge" value={profile.currentAge} onChange={handleChange} className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">Retirement Age</label>
            <input type="number" name="retirementAge" value={profile.retirementAge} onChange={handleChange} className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">Monthly Salary</label>
            <input type="number" name="monthlySalary" value={profile.monthlySalary} onChange={handleChange} className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">Current Savings</label>
            <input type="number" name="currentSavings" value={profile.currentSavings} onChange={handleChange} className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">Monthly Rent</label>
            <input type="number" name="monthlyRent" value={profile.monthlyRent} onChange={handleChange} className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">Monthly Living Expenses</label>
            <input type="number" name="monthlyLivingExpenses" value={profile.monthlyLivingExpenses} onChange={handleChange} className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">Monthly Investment</label>
            <input type="number" name="monthlyInvestment" value={profile.monthlyInvestment} onChange={handleChange} className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">Annual Salary Growth (%)</label>
            <input type="number" name="annualSalaryGrowthPercent" value={profile.annualSalaryGrowthPercent} onChange={handleChange} className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">Annual Inflation (%)</label>
            <input type="number" name="annualInflationPercent" value={profile.annualInflationPercent} onChange={handleChange} className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">Annual Investment Return (%)</label>
            <input type="number" name="annualInvestmentReturnPercent" value={profile.annualInvestmentReturnPercent} onChange={handleChange} className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">Financial Goal Target Amount ($)</label>
            <input type="number" name="goalTargetAmount" value={profile.goalTargetAmount} onChange={handleChange} className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">Financial Goal Target Age</label>
            <input type="number" name="goalTargetAge" value={profile.goalTargetAge} onChange={handleChange} className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" />
          </div>

        </div>

        <div className="flex justify-end">
          <button type="submit" className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700 text-sm font-medium">
            Save Profile
          </button>
        </div>
      </form>
    </div>
  );
};

export default Profile;
