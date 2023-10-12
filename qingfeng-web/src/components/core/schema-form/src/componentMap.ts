/**
 * Component list, register here to setting it in the form
 */
import {
  Input,
  Select,
  Radio,
  Checkbox,
  AutoComplete,
  Cascader,
  DatePicker,
  InputNumber,
  Switch,
  TimePicker,
  TreeSelect,
  Tree,
  Slider,
  Rate,
  Divider,
  Upload,
  Alert,
} from 'ant-design-vue';
import SelectOrg from '@/components/core/schema-form/src/components/SelectOrganize.vue';
import Tinymce from '@/components/core/schema-form/src/components/Tinymce.vue';
import Subform from '@/components/core/schema-form/src/components/Subform.vue';

const componentMap = {
  Input,
  InputGroup: Input.Group,
  InputPassword: Input.Password,
  InputSearch: Input.Search,
  InputTextArea: Input.TextArea,
  InputNumber,
  AutoComplete,
  Select,
  TreeSelect,
  Tree,
  Switch,
  RadioGroup: Radio.Group,
  Checkbox,
  CheckboxGroup: Checkbox.Group,
  Cascader,
  Slider,
  Rate,
  DatePicker,
  MonthPicker: DatePicker.MonthPicker,
  RangePicker: DatePicker.RangePicker,
  WeekPicker: DatePicker.WeekPicker,
  TimePicker,
  Upload,

  Divider,
  SelectOrg,
  Tinymce,
  Subform,
  Alert,
};

export type ComponentMapType = keyof typeof componentMap;

export { componentMap };
