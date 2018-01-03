DROP TABLE IF EXISTS tweets;

CREATE TABLE tweets (
  id text PRIMARY KEY,
  tweetid text,
  user text,
  tweet text,
  createddatetime timestamp
) WITH comment='tweets for disruptor testing'
  AND COMPACT STORAGE
  AND read_repair_chance = 1.0;
