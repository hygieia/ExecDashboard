import { Component, OnInit, Input } from '@angular/core';
import { DevopscupScores } from '../../../../../shared/domain-models/devopscupScores';

@Component({
  selector: 'app-devopscup-graph',
  templateUrl: './devopscup-graph.component.html',
  styleUrls: ['./devopscup-graph.component.scss']
})
export class DevopscupGraphComponent implements OnInit {
  @Input() public devopscupScores: DevopscupScores = null;
  @Input() public metricsName: string = null;
  @Input() public showGraph: boolean = false;
  curMonth: number = new Date().getMonth() + 1;
  metricsTitle: string = '';
  metricsMaturity: number = 0;
  metricsVal: number = 0;
  metricsColor: String = 'yellowgreen';
  metricsBaseLine: number = 0;
  metricsWidth: number = 0;
  metricsDisplayValue: string = '';
  firstLabel: string = '';
  secondLabel: string = '';
  maturityLabel: string = '';
  metrcisUnit: string = '';

  correctNull(obj: any): any {
    return (obj === undefined || obj === null || obj === '') ? 0 : obj;
  }

  public isNotNullVal(obj: any): boolean {
    if (obj !== null && obj !== undefined) {
      return true;
    }
    return false;
  }
  ngOnInit() {

    if (this.isNotNullVal(this.devopscupScores)) {
      if (this.isNotNullVal(this.devopscupScores.enggExcel)) {
        switch (this.metricsName) {
          case 'dc': this.metricsTitle = 'Deployment Cadence';
            this.metricsVal = this.correctNull(this.devopscupScores.enggExcel.deploymentCadencyValue);
            this.metricsBaseLine = this.correctNull(this.devopscupScores.enggExcel.deploymentCadencyValueBL);
            this.metricsMaturity = this.correctNull(this.devopscupScores.enggExcel.deploymentCadencyMaturity);
            this.firstLabel = '>30 days';
            this.secondLabel = '1 hour';
            this.maturityLabel = '% from ' + this.getMonthValue();
            this.metrcisUnit = this.metricsVal > 1 ? 'days' : 'day';
            break;
          case 'ct': this.metricsTitle = 'Cycle Time';
            this.metricsVal = this.correctNull(this.devopscupScores.enggExcel.cycleTimeValue);
            this.metricsBaseLine = this.correctNull(this.devopscupScores.enggExcel.cycleTimeValueBL);
            this.metricsMaturity = this.correctNull(this.devopscupScores.enggExcel.cycleTimeMaturity);
            this.firstLabel = '>30 days';
            this.secondLabel = '<1 hour';
            this.maturityLabel = '% from ' + this.getMonthValue();
            this.metrcisUnit = this.metricsVal > 1 ? 'days' : 'day';
            break;
          case 'mttr': this.metricsTitle = 'MTTR';
            this.metricsVal = this.correctNull(this.devopscupScores.enggExcel.mttrValue);
            this.metricsBaseLine = this.correctNull(this.devopscupScores.enggExcel.mttrValueBL);
            this.metricsMaturity = this.correctNull(this.devopscupScores.enggExcel.mttrMaturity);
            this.firstLabel = '>30 days';
            this.secondLabel = '<1 hour';
            this.maturityLabel = '% from Last 365 days';
            this.metrcisUnit = this.metricsVal > 1 ? 'days' : 'day';
            break;
          case 'mtbf': this.metricsTitle = 'MTBF';
            this.metricsVal = this.correctNull(this.devopscupScores.enggExcel.mtbfValue);
            this.metricsBaseLine = this.correctNull(this.devopscupScores.enggExcel.mtbfValueBL);
            this.metricsMaturity = this.correctNull(this.devopscupScores.enggExcel.mtbfMaturity);
            this.firstLabel = '>1 day';
            this.secondLabel = '>365 days';
            this.maturityLabel = '% from Last 365 days';
            this.metrcisUnit = this.metricsVal > 1 ? 'days' : 'day';
            break;
          case 'cf': this.metricsTitle = 'Change Failure Rate';
            this.metricsVal = this.correctNull(this.devopscupScores.enggExcel.cfRateValue);
            this.metricsBaseLine = this.correctNull(this.devopscupScores.enggExcel.cfRateValueBL);
            this.metricsMaturity = this.correctNull(this.devopscupScores.enggExcel.cfRateMaturity);
            this.firstLabel = '> 45%';
            this.secondLabel = '<1%';
            this.maturityLabel = '% from ' + this.getMonthValue();
            this.metrcisUnit = '%';
            break;
          case 'sec': this.metricsTitle = 'Security';
            this.metricsVal = this.correctNull(this.devopscupScores.enggExcel.fortifyValue);
            this.metricsBaseLine = this.correctNull(this.devopscupScores.enggExcel.fortifyValueBL);
            this.metricsMaturity = this.correctNull(this.devopscupScores.enggExcel.fortifyMaturity);
            this.firstLabel = '> 1 Vul in C,H,M';
            this.secondLabel = '< 0 Vul in C,H,M';
            this.maturityLabel = '% from ' + this.getMonthValue();
            this.metricsDisplayValue = this.correctNull(this.devopscupScores.enggExcel.fortifyCriticalVul) + ' C ,' + this.correctNull(this.devopscupScores.enggExcel.fortifyHighVul) + ' H, ' + this.correctNull(this.devopscupScores.enggExcel.fortifyMediumVul) + ' M';
            break;
          case 'tech': this.metricsTitle = 'Tech Byte';
            this.metricsVal = this.correctNull(this.devopscupScores.enggExcel.techByteValue);
            this.metricsMaturity = this.metricsVal;
            this.firstLabel = '<1';
            this.secondLabel = '>3  ';
            this.maturityLabel = 'Sessions';
            this.metrcisUnit = '';
            this.metricsWidth = this.correctNull(this.devopscupScores.enggExcel.techByteMaturity);
            break;
          default: break;
        }
      }


      if (this.metricsVal === 0 && this.metricsMaturity === 0) {
        this.metricsDisplayValue = this.getMessage(this.metricsTitle);
        this.metricsColor = 'darkgray';
        this.metricsWidth = 100;
      }
      else {

        if (this.metricsName !== 'sec') {
          this.metricsDisplayValue = this.metricsVal + ' ' + this.metrcisUnit;
        }
        this.metricsColor = 'yellowgreen';
        if (this.metricsName !== 'tech') {
          this.metricsWidth = Math.round(this.metricsMaturity);
        }

      }
    }
  }


  public getMessage(type: String) {
    if (type != 'Tech Byte') {
      return 'This Collector is not configured,Please configure  ' + type + ' Collector to get points';
    }
    else {
      return 'No Tech Byte value for this application';
    }
  }
  public getMonthValue() {
    let quarter = Math.ceil(this.curMonth / 3);
    if (quarter === 2) {
      return 'March 1';
    } else if (quarter === 3) {
      return 'June 1';
    }
    else {
      return 'September 1';
    }
  }


}
