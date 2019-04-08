import { Strategy } from '../../../../../shared/strategies/strategy';
import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { MetricSegmentationModel } from '../../../shared/component-models/metric-segmentation-model';
import { Injectable } from '@angular/core';

@Injectable()
export class CloudSegmentationStrategy implements Strategy<MetricSummary, MetricSegmentationModel> {
    parse(model: MetricSummary): MetricSegmentationModel {
        const cost = model.counts
            .filter(i => i.label['type'] === 'cost')
            .map(c => c.value)
            .reduce((a, b) => a + b, 0);

        const unencryptedEbs = model.counts
            .filter(i => i.label['type'] === 'unencryptedEBS')
            .map(c => c.value)
            .reduce((a, b) => a + b, 0);

        const encryptedEbs = model.counts
            .filter(i => i.label['type'] === 'encryptedEBS')
            .map(c => c.value)
            .reduce((a, b) => a + b, 0);

        const totalEbs = encryptedEbs + unencryptedEbs;

        let ebspercent = 0;
        if (totalEbs != 0)
            ebspercent = (encryptedEbs / totalEbs) * 100;
        const unencryptedS3 = model.counts
            .filter(i => i.label['type'] === 'unencryptedS3')
            .map(c => c.value)
            .reduce((a, b) => a + b, 0);

        const encryptedS3 = model.counts
            .filter(i => i.label['type'] === 'encryptedS3')
            .map(c => c.value)
            .reduce((a, b) => a + b, 0);

        const totalS3 = encryptedS3 + unencryptedS3;
        let s3percent = 0;
        if (totalS3 != 0)
            s3percent = (encryptedS3 / totalS3) * 100;
        const complaince = Math.round(ebspercent + s3percent) / 2;
        const migratedCount = model.counts
            .filter(i => i.label['type'] === 'migrationEnabled')
            .map(c => c.value)
            .reduce((a, b) => a + b, 0);
        const costOptimizedApps = model.counts
            .filter(i => i.label['type'] === 'costOptimized')
            .map(c => c.value)
            .reduce((a, b) => a + b, 0);
        const amiCount = model.counts
            .filter(i => i.label['type'] === 'Total AMI Count')
            .map(c => c.value)
            .reduce((a, b) => a + b, 0);
        const elbCount = model.counts
            .filter(i => i.label['type'] === 'Total ELB Count')
            .map(c => c.value)
            .reduce((a, b) => a + b, 0);

        const rows = [
            { name: 'AWS Compliance Report', order: 1, value: complaince, unit: '%' },
            { name: 'Prod Migration Enabled Apps', order: 2, value: migratedCount, unit: '' }
        ];

        return {
            rows: rows.map(row => {

                return {
                    name: row.name,
                    order: row.order,
                    unit: row.unit,
                    columns: [{
                        name: null,
                        order: null,
                        value: row.value
                    }]
                };
            })
        };
    }
}


