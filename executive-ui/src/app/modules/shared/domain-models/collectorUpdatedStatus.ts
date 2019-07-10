export class CollectorUpdatedStatus {
  type: string;
  collectionName: string;
  collectorUpdateTime: number;
  collectionUpdatedTime: number;
  appCount: number;
  duration?: number;
  collectorStartTime?: number;
}
