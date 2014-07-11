for i = 1, #sim_data, 3 do
				local sim_temp = {}
				sim_temp[1] = string.byte(sim_data, i)
				sim_temp[2] = string.byte(sim_data, i+1)
				sim_temp[3] = string.byte(sim_data, i+2)
				table.insert(sim_table, sim_temp)
			end
