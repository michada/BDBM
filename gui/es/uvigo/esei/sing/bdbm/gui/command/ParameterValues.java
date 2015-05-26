package es.uvigo.esei.sing.bdbm.gui.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import es.uvigo.ei.sing.yaacli.DefaultValuedOption;
import es.uvigo.ei.sing.yaacli.MultipleParameterValue;
import es.uvigo.ei.sing.yaacli.Option;
import es.uvigo.ei.sing.yaacli.ParameterValue;
import es.uvigo.ei.sing.yaacli.Parameters;
import es.uvigo.ei.sing.yaacli.SingleParameterValue;

public class ParameterValues extends Observable implements ParameterValuesReceiver, Parameters {
		private final List<Option<?>> options;
		private final Map<Option<?>, ParameterValue<?>> values;
		
		public ParameterValues(List<Option<?>> options) {
			this.values = new HashMap<Option<?>, ParameterValue<?>>();
			this.options = Collections.unmodifiableList(
				new ArrayList<Option<?>>(options)
			);
			
			for (Option<?> option : options) {
				if (option instanceof DefaultValuedOption) {
					final DefaultValuedOption<?> dvOption = 
						(DefaultValuedOption<?>) option;
					
					if (option.isMultiple()) {
						this.values.put(option, new MultipleParameterValue(Arrays.asList(dvOption.getDefaultValue())));
					} else {
						this.values.put(option, new SingleParameterValue(dvOption.getDefaultValue()));
					}
				}
			}
		}

		@Override
		public <T> T getSingleValue(Option<T> option) {
			return option.getConverter().convert(
				(SingleParameterValue) this.values.get(option)
			);
		}

		@Override
		public <T> List<T> getAllValues(Option<T> option) {
			return option.getConverter().convert(
				(MultipleParameterValue) this.values.get(option)
			);
		}
		
		@Override
		public List<String> getAllValuesString(Option<?> option) {
			return ((MultipleParameterValue) this.values.get(option)).getValue();
		}
		
		@Override
		public String getSingleValueString(Option<?> option) {
			return ((SingleParameterValue) this.values.get(option)).getValue();
		}

		@Override
		public boolean hasFlag(Option<?> option) {
			return this.values.containsKey(option);
		}

		@Override
		public boolean hasOption(Option<?> option) {
			return this.options.contains(option);
		}
		
		@Override
		public boolean removeValue(Option<?> option) {
			final ParameterValue<?> removedValue = this.values.remove(option);
			
			if (removedValue != null) {
				this.setChanged();
				this.notifyObservers(option);
				
				return true;
			} else {
				return false;
			}
		}
		
		public Map<Option<?>, ParameterValue<?>> getValues() {
			return Collections.unmodifiableMap(this.values);
		}
		
		public boolean hasValue(Option<?> option) {
			return this.values.containsKey(option);
		}
		
		@Override
		public String getValue(Option<?> option) {
			return this.hasValue(option) ?
				this.getSingleValueString(option) : null;
		}
		
		@Override
		public List<String> getValues(Option<?> option) {
			return this.getAllValuesString(option);
		}
		
		@Override
		public void setValue(Option<?> option, String value) {
			if (option.isMultiple()) {
				throw new IllegalArgumentException("option is not simple");
			}
			
			if (value == null) {
				this.values.remove(option);
			} else {
				this.values.put(option, new SingleParameterValue(value));
			}
			
			this.setChanged();
			this.notifyObservers(option);
		}
		
		@Override
		public void setValue(Option<?> option, List<String> value) {
			if (!option.isMultiple()) {
				throw new IllegalArgumentException("option is not multiple");
			}
			
			if (value == null || value.isEmpty()) {
				this.values.remove(option);
			} else {
				this.values.put(option, new MultipleParameterValue(value));
			}
			
			this.setChanged();
			this.notifyObservers(option);
		}
		
		public boolean isComplete() {
			for (Option<?> option : this.options) {
				if (!option.isOptional() &&
					(!this.values.containsKey(option) ||
					 (option.isMultiple() && !option.getConverter().canConvert((MultipleParameterValue) this.values.get(option))) ||
					 (!option.isMultiple() && !option.getConverter().canConvert((SingleParameterValue) this.values.get(option)))
					)
				) {
					return false;
				}
			}
			
			return true;
		}
	}